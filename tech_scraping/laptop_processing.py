

import pandas as pd
import re
import glob
from bing_image_urls import bing_image_urls as bg

class LaptopProcessing:
    '''This Class is responsable for finalizing preprocessing and generation tasks'''

    def handle_laptops_table(self,table=None):
        if table is not None:

            #Using list comprehension to remove unwanted data
            table=[x for x in table if x not in [ "Ultra Low",
                                                        "Low",
                                                        "Medium",
                                                        "Medium-High",
                                                        "High",
                                                        "Very High" ]]
            

            laptop_list=[]
            row=[table[0]]

            #This loop is used for sperating elements and get every laptop
            #attribute in specific row "list" then append it to a bigger list
            for element in table[1:]:
                if not re.search(r"^\$\d+",element):
                    row.append(element)
                else:
                    laptop_list.append(row)
                    row=[element]
            
            #Append last row because the loop break without founding $ sign 
            #which is important to append the whole row
            laptop_list.append(row[:12])

            #for removing empty string at the end of the row
            [row.pop(-1) for row in laptop_list if len(row)==13]
                
                    

            return laptop_list
        else:
            pass
        


    def to_num(self,x):
        return int("".join([i for i in x.split(".")[0] if i.isdigit()]))

    def specify_budget(self, x, cur="USD"):

        if cur == "USD":
            return "Extreme-Low/Low" if x < 718 else "Medium/Med-High" if x < 1399 else "High/Extreme-High"
            
        elif cur == "EGP":
            return "Low" if x <= 37345 else "Medium" if x < 52000 else "High/Extreme-High"
                
        
    def laptops_list_to_df(self, laps_list=None)->pd.DataFrame:

        # Return an empty DataFrame if laps_list is None
        if laps_list is None:
            return pd.DataFrame()

        # Define the list of attribute names
        attributes_names = ["Price", "Model", "CPU","Graphics",
                            "RAM", "HDD", "SSD","Display", "Battery",
                            "Quality", "Weight", "Thickness"]
        
        # Initialize an empty dictionary to store the attributes
        attributes_dict = {attr_name: [] for attr_name in attributes_names}

        # Populate the attributes dictionary from the laps_list
        for row in laps_list:
            for i, value in enumerate(row):
                attributes_dict[attributes_names[i]].append(value)

        # Create a DataFrame from the attributes dictionary
        attr_df = pd.DataFrame(attributes_dict)

        return attr_df

        

    def concatenate_laptops_dfs(self,files=None):
        if files is None:
            pass
        else:
            return pd.concat([ pd.read_csv(file)
                                            for file in files],ignore_index=True) 
    

    
    def lap_csv_files_handler(self):
        deal_files=glob.glob("output/lap_deals_*.csv")
        top_lap_file=glob.glob("output/best_laptops.csv")[0]
        

        #Loop to handle deals/offers csv files
        for file in deal_files:
            deal_df=pd.read_csv(file)
            deal_df["Budget"]= deal_df["Price"].map(lambda x: self.specify_budget(self.to_num(x),"EGP"))
            deal_df["Final_Budget"]=deal_df["Price"]+" '"+deal_df["Budget"]+"'"
            deal_df["Notes"]=deal_df[["Best_For","Deal_Link","Deal_Stat"]].agg(",".join,axis=1)
            deal_df["Rate_From_10"]=deal_df["Budget"].map(lambda x:"-")
            deal_df["Type"]=deal_df["Budget"].map(lambda x:"Deal")
            deal_df["Brand"] = deal_df.apply(lambda row: row["Model"].split(" ")[0] if row["Brand"] == "-" else row["Brand"], axis=1) 
            deal_df.to_csv(file,index=False)
        
        #Handling Top laptops csv file
        top_lap_df=pd.read_csv(top_lap_file)
        top_lap_df["Final_Budget"]=top_lap_df["Price"]+" '"+top_lap_df["Budget"]+"'"
        top_lap_df["Notes"]=top_lap_df["Best_For"].map(lambda x:str(x))
        top_lap_df["Brand"]=top_lap_df["Model"].map(lambda x:x.split(" ")[0])

        if "Image_Url" not in list(top_lap_df.columns):
            top_lap_df["Image_Url"]=top_lap_df["Model"].map(lambda x:bg(x,limit=1)[0])

        top_lap_df["Type"]=top_lap_df["Best_For"].map(lambda x:"Top")
        top_lap_df.fillna("-",inplace=True)
        top_lap_df.to_csv(top_lap_file,index=False)


    def convert_files_to_final_df(self,files=None):

        if files is None:
            pass

        else:
            df=self.concatenate_laptops_dfs(files)

            #Handling Missing Brand Names
            df["Brand"]=df["Model"].map(lambda x:x.split(" ")[0]if x=="-" else df["Brand"][df["Model"]==x].values[0])

            #List Comperhension to get instersected columns and drop unwanted columns 
            [df.drop(columns=[x],axis=1,inplace=True) for x in df.columns if df[x].isna().sum()>=43]

            df['Model']=df['Model'].map(lambda x: str(x)[:40] if len(str(x))>=40 else x)

            

            df.to_csv("output/final_result.csv",index=False)

            return df        
