import web_driver
from abc import ABC

#Constants
LAPTOPS_CATEGORY_LIST=["gaming","mainstream","convertible","programming","ultraportable"]
LAPTOPS_CATEGORY_LIST2=["Student","Gaming","Professional","360X"]

class TechScraper(ABC):

    def __init__(self,category=None,tech_list=None):
        self.tech=tech_list
        self.cat=category
        self.drv=web_driver.DriverInit()