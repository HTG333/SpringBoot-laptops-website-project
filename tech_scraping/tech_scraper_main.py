
import pandas as pd
import glob
import os


import laptop_processing as lap_pro
import laptop_db as lap_db
import top_laptops_scraper as top_laps
import laptop_deals_scraper_2b as lap_deals_2b
import tech_scraper_abstract as tsa






        





class TechScraperFacade:

    def __init__(self,category=None,tech_list=None):
        self.laptop_deals_2b_scraper=lap_deals_2b.LaptopDealsScraper2B(category,tech_list)
        self.top_laptops_scraper=top_laps.TopLaptopsScraper(category,tech_list)
        self.processing=lap_pro.LaptopProcessing()
        self.lap_db=lap_db.LaptopDB()
        
        
    def get_and_cache_laptops_data(self):

        '''
        this method includes the whole scraping process
        from scraping websites links and getting laptops 
        specifications to integrate and insert laptops data to database

        '''

        top_laps_df=self.top_laptops_scraper.get_all_top_laps_df(tsa.LAPTOPS_CATEGORY_LIST)
        self.lap_db.laptop_df_db_operations(top_laps_df,"cache","top_laptops")


        self.laptop_deals_2b_scraper.collect_laptops_links(tsa.LAPTOPS_CATEGORY_LIST2)

        #Reading collected laptop lists
        df=pd.read_csv("output/laptop_offers_links.csv")
        
        

        #loop for scraping links from each column in the links DataFrame for each category
        for laptop_column in df.columns:
            #List comprehension to get only valid links
            column_valid_lap_list=[x for x in df[laptop_column] if str(type(x))=="<class 'str'>"]

            self.laptop_deals_2b_scraper.scrape_laptop_deals(
                                            lap_links_list= column_valid_lap_list,
                                            category=laptop_column)



        self.processing.lap_csv_files_handler()

        files=glob.glob("output/best_laptops.csv")+glob.glob("output/lap_*.csv")
        self.processing.convert_files_to_final_df(files)

        df=pd.read_csv("output/final_result.csv")

        self.lap_db.df_to_laptop_db(df)
        self.lap_db.laptop_df_db_operations(df,"cache")





if '__main__'==__name__:

    if not os.path.exists("./output"):
        os.makedirs("./output")

    TechScraperFacade().get_and_cache_laptops_data()







