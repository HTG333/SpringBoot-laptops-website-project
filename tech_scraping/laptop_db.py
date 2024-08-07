import datetime
import pandas as pd

import psycopg2
from sqlalchemy import create_engine
from sqlalchemy import text as sa_text
from sqlalchemy.orm import sessionmaker



class LaptopDB:
        
    def df_to_laptop_db(self,df=None):
        if df is None:
            pass

        else:
            self.laptop_df_db_operations(pd.DataFrame(),"truncate","laptop_data")

            conn = psycopg2.connect(database = "Tech",
                                    user = "postgres",
                                    password = "admin",
                                    host = "127.0.0.1",
                                    port = "5435")

            cur = conn.cursor()


            for i in range(len(df)):
                cur.execute("INSERT INTO laptop_data(lap_id,model,brand,hdd,ssd,display,"+
                            "graphics,notes,budget,cpu,dev_photo_url,"+
                            "rate,ram,type,updated_on,created_on,created_by)"+
                            " VALUES(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)",
                        [
                            i,
                         df["Model"][i],
                         df["Brand"][i],
                         df["HDD"][i],
                         df["SSD"][i],
                         df["Display"][i],
                         df["Graphics"][i],
                         df["Notes"][i],
                         df["Final_Budget"][i],
                         df["CPU"][i],
                         (df["Image_Url"][i] if df["Image_Url"][i] !='-' else None),
                         df["Rate_From_10"][i],
                         df["RAM"][i],
                         df["Type"][i],
                         
                         datetime.datetime.now(),
                         datetime.datetime.now(),2  ])

            conn.commit()

            conn.close()

    def laptop_df_db_operations(self,df:pd.DataFrame,mode="get",table="df_cache"):

        engine = create_engine('postgresql://postgres:admin@localhost:5435/Tech')

        if mode=='cache':
            df.to_sql(table, con=engine,if_exists='replace',index=False)
            return "DataFrame Data cached successfully!"

        elif mode=='get':
            try:
                df=pd.read_sql_query("select * from "+table,con=engine)
                return df
            except Exception as e:
                return None
            
        elif mode=="truncate":
            try:
                Session = sessionmaker(bind=engine)
                session = Session()
                session.execute(sa_text('TRUNCATE TABLE '+table))
                session.commit()
                session.close()
                return "Table Data Deleted successfully!"

            except Exception as e:
                return None


