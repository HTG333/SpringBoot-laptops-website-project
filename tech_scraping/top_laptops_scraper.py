from selenium.webdriver.common.by import By
import requests 
from bs4 import BeautifulSoup


import pandas as pd
from threading import Thread



import tech_scraper_abstract as ts
import laptop_processing as lap_pro
import laptop_db as lap_db

class TopLaptopsScraper(ts.TechScraper):

    def __init__(self,category=None,tech_list=None):
        super().__init__(category,tech_list)
        self.pre=lap_pro.LaptopProcessing()
        self.lap_db=lap_db.LaptopDB()

    def get_top_laptops_by_category_df(self,category=None)->pd.DataFrame:

        if category in ts.LAPTOPS_CATEGORY_LIST:


            #Intialize Web Driver
            drv=self.drv.initialize_driver("google")

            #Getting to the website 
            drv.get("https://www.lappylist.com/laptops/best-"+category+"-laptops/")
            
            #Scrapping the table by CSS SELECTOR
            top_laps_table=[x.text for x in drv.find_elements(By.CSS_SELECTOR,
                                                            "#lappylist-table > tbody > tr > td")]
            
            #Scrapping Span Tags in the table that include img to get laptop image url
            image_urls=[x.get_attribute("data-title") for x in drv.find_elements(By.CSS_SELECTOR,
                                    "#lappylist-table > tbody > tr > td.hover-hl-td.model-td.inside-table > span"
                                                            )]
            #Getting Laptops Images URLS from img tag
            image_urls=[BeautifulSoup(x,"lxml").find("img")["src"] for x in image_urls]

            

            #Close The Driver
            drv.close()


            

            #Handling the data in the table 
            laps_list=self.pre.handle_laptops_table(top_laps_table)


            #Convert it to a DataFrame 
            laptops_df=self.pre.laptops_list_to_df(laps_list)
            laptops_df["Budget"]= laptops_df["Price"].map(lambda x: self.pre.specify_budget(self.pre.to_num(x)))
            laptops_df["Best_For"]= laptops_df["Price"].map(lambda x: category)
            laptops_df["Rate_From_10"]=laptops_df["Quality"].map(lambda x: x.split("/")[0])
            laptops_df["Image_Url"]=image_urls[:-1]
            laptops_df.drop(columns=["Quality"],inplace=True)
            
            
            #Return the DataFrame
            return laptops_df
            
            
            
            
        else:
            pass

    def process_category(self,category, result_list:list):
        result_list.append(self.get_top_laptops_by_category_df(category))


    def get_all_top_laps_df(self, cat_list=None)->pd.DataFrame:
        if cat_list is None:
            pass
        else:
            df_list = []
            threads = []
            result_list = []

        try:
                
                response = requests.get("https://www.lappylist.com/",timeout=4)
                
                if response.status_code==200:

                    for category in cat_list:
                        thread =Thread(target=self.process_category, args=(category, result_list))
                        threads.append(thread)
                        thread.start()

                    for thread in threads:
                        thread.join()

                

                    df = pd.concat(result_list, ignore_index=True)

                    df = df.groupby([
                        x for x 
                        in df.columns
                        if x != "Best_For"
                            ])["Best_For"].apply(",".join).reset_index().drop_duplicates(["Model"], keep="first")

                    df.to_csv("output/best_laptops.csv", index=False)

                    return df
                
                else:
                    top_laps_df=self.lap_db.laptop_df_db_operations(pd.DataFrame(),"get","top_laptops")
                    top_laps_df["Image_Url"]=top_laps_df["Image_Url"][top_laps_df['Type']=='Deal'].map(lambda x:x)
                    top_laps_df["Image_Url"]=top_laps_df["Image_Url"].fillna('')
                    top_laps_df.to_csv("output/best_laptops.csv", index=False)
                    return top_laps_df
        
        except requests.exceptions.RequestException as e:
                top_laps_df=self.lap_db.laptop_df_db_operations(pd.DataFrame(),"get","top_laptops")
                top_laps_df["Image_Url"]=top_laps_df["Image_Url"][top_laps_df['Type']=='Deal'].map(lambda x:x)
                top_laps_df["Image_Url"]=top_laps_df["Image_Url"].fillna('')
                top_laps_df.to_csv("output/best_laptops.csv", index=False)
                return top_laps_df
        
        
