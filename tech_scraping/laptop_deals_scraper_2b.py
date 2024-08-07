from selenium.webdriver.common.by import By
import requests 
from bs4 import BeautifulSoup


import pandas as pd
import time
from threading import Thread




import laptop_processing as lap_pro
import laptop_db as lap_db
import tech_scraper_abstract as ts




class LaptopDealsScraper2B(ts.TechScraper):

    def __init__(self,category=None,tech_list=None):
        super().__init__(category,tech_list)
        self.pre=lap_pro.LaptopProcessing()
        self.lap_db=lap_db.LaptopDB()


    
  
    # Method to process each category and collect laptop links
    def process_category(self, lap_for, lap_for_code, collected_links_dict):
        try:
            drv = self.drv.initialize_driver("google")
            drv.get("https://2b.com.eg/en/computers/laptops.html?laptop_category=" + str(lap_for_code))
            lap_elements = drv.find_elements(By.CSS_SELECTOR, "#layer-product-list > div.products.wrapper.grid.columns4.products-grid > ol > li.item.product.product-item > div > div > a")
            lap_links = [x.get_attribute("href") for x in lap_elements]
        except Exception as e:
            lap_links = []
        
        collected_links_dict[lap_for] = lap_links
        drv.close()

    # Method to collect laptop links using threading for efficiency
    def collect_laptops_links(self, lap_for_list=ts.LAPTOPS_CATEGORY_LIST2)->pd.DataFrame:
        # Check if lap_for_list is empty or None
        if lap_for_list is None or len(lap_for_list) == 0:
            return

        collected_links_dict = {}  # Dictionary to store collected links

        category_mapping = {  # Mapping of category to code
            "Student": 1059,
            "Gaming": 1061,
            "Professional": 1060,
            "360X": 7503
        }

        threads = []  # List to store threads
        
        # Create threads for each category
        for lap_for in lap_for_list:
            lap_for_code = category_mapping.get(lap_for, None)
            thread =Thread(target=self.process_category, args=(lap_for, lap_for_code, collected_links_dict))
            threads.append(thread)
            thread.start()

        # Wait for all threads to finish
        for thread in threads:
            thread.join()

        # Convert collected links to DataFrame and save to CSV
        collected_links_df = pd.DataFrame({key: pd.Series(value) for key, value in collected_links_dict.items()})
        collected_links_df.to_csv("output/laptop_offers_links.csv", index=False)

        return collected_links_df
    
    def scrape_laptop_deals(self,lap_links_list=None,category=None):
        
        if lap_links_list is None or len(lap_links_list)==0:
            pass

        else:

            prices, models, brands, cpus, graphics, rams, hdd, ssd, displays, notes, best_for_list, images_urls, deals_stats,device_link = [[] for _ in range(14)]


            attributes=[ prices,models,cpus,graphics,
                         rams,hdd,ssd,displays,
                         notes,brands,best_for_list,
                         images_urls ,deals_stats,device_link ]
            
            attributes_names=[ "Price","Model","CPU",
                            "Graphics","RAM","HDD",
                            "SSD","Display","Notes","Brand",
                            "Best_For",	"Image_Url" ,"Deal_Stat","Deal_Link" ]
            
            def scrape_laptop_info(lap_link):
                
                try:
                    
                    lap_page=requests.get(lap_link)
                    spec_table=pd.read_html(lap_page.content)[0]
                    soup=BeautifulSoup(lap_page.text,"html.parser")
                    time.sleep(1)

                except Exception as e:
                    print(e)

                try:
                    lcat=category
                except Exception as e:
                    lcat="-"

                try:
                    limage=soup.find("img",{"alt":"main product photo"})["src"]
                except Exception as e:
                    limage="-"
                
                try:
                    lprice=soup.find("span",{"data-price-type":"finalPrice"}).text.strip()

                except Exception as e:
                    lprice="-"


                try:                

                    lstat=soup.find("div",{"class":"stock available"})
                    left_only=soup.find("div",{"class":"availability only"})
                    
                    if left_only is not None:
                        lstat= lstat.text.strip()+" "+left_only.text.strip()
                    
                    elif left_only is None:
                        lstat=lstat.text.strip()

                except Exception as e:
                    lstat="Out Of Stock"


            
                try:
                    model=spec_table[1][spec_table[0]=="Model"].values[0]
                except Exception as e:
                    #Getting name from laptop link if not found
                    model=" ".join(lap_link.split("/")[4].split("-")).replace(".html","")

                try:
                    brand=spec_table[1][spec_table[0]=="Brand"].values[0]
                except Exception as e:
                    brand="-"

                
                try:

                    if "Processor" in list(spec_table[0]):
                        cpu=spec_table[1][spec_table[0]=="Processor"].values[0]

                    if "Processor Information" in list(spec_table[0]):
                        cpu=spec_table[1][spec_table[0]=="Processor Information"].values[0]

                except Exception as e:
                    cpu="-"


                try:
                    ram=spec_table[1][spec_table[0]=="RAM"].values[0]
                except Exception as e:
                    ram="-"
                try:
                    hard_disk=spec_table[1][spec_table[0]=="Hard Disk Type"].values[0]
                except Exception as e:
                    hard_disk="-"

                try:      
                    
                    if  "Graphics Card" in list(spec_table[0]):
                        graphics_card=spec_table[1][spec_table[0]=="Graphics Card"].values[0]
                        

                    if  "Graphics Card Details" in list(spec_table[0]):
                        graphics_card=spec_table[1][spec_table[0]=="Graphics Card Details"].values[0]


                except Exception as e:
                    graphics_card="-"


                try:
                    display=spec_table[1][spec_table[0]=="Display Type"].values[0]

                except Exception as e:
                    display="-"
                    
                brands.append(brand)
                models.append(model)
                displays.append(display)
                cpus.append(cpu)
                rams.append(ram)
                graphics.append(graphics_card)
                notes.append(lcat)
                images_urls.append(limage)
                prices.append(lprice)
                best_for_list.append(lcat)
                deals_stats.append(lstat)
                device_link.append(lap_link)
                ssd.append(hard_disk)
                hdd.append(hard_disk)
                

                if "SSD" not in ssd[len(ssd)-1]:
                    del ssd[len(ssd)-1]
                    ssd.append("Unspecified")

                if "HDD" not in hdd[len(hdd)-1]:
                    del  hdd[len(hdd)-1]
                    hdd.append("Unspecified")

                if "HDD" not in hdd[len(hdd)-1] and "SSD" not in ssd[len(ssd)-1]:
                    del  ssd[len(ssd)-1]
                    ssd.append(hard_disk)
                    del  hdd[len(hdd)-1]
                    hdd.append("Unspecified")

            threads = []
            for lap_link in lap_links_list:
                thread = Thread(target=scrape_laptop_info, args=(lap_link,))
                threads.append(thread)
                thread.start()

            for thread in threads:
                thread.join()


            attributes_dict={}

            for index,attribute in enumerate(attributes):
                attributes_dict[attributes_names[index]]=attribute
                    
                    
            laptop_deals_df=pd.DataFrame(attributes_dict)

            laptop_deals_df.to_csv("output/lap_deals_"+str(category)+".csv",index=False)

            return laptop_deals_df
        

