# Project Description

This project serves as a comprehensive web scraping and data processing tool, aiming to extract top tech information and tech offers from multiple websites. The project focuses on aggregating and collecting tech offers and data from various websites, making it convenient for users to access and compare tech offers, specify budgets, and gather detailed information from a single platform. The primary focus is on laptops offers and top laptops, with the potential for expansion to include other tech products in the future.

The project utilizes Selenium, Requests, Beautiful Soup, and preprocessing techniques to scrape data efficiently. Multiple classes such as `TechScraper`, `TechScraperFacade`, `TopLaptopsScraper`, `LaptopDealsScraper2B`, `LaptopProcessing`, `WebDriverInit`, and `LaptopDB` facilitate the scraping, processing, and storage of data.

## Key Features

- **Scalability**: Designed for scalability to incorporate additional tech scrapers for a variety of products like phones, TVs, smartwatches, etc., enhancing the user experience with a wider range of tech offers.

- **Structural Design Pattern**: Implementing design patterns like the Facade pattern ensures the project's codebase is maintainable and adaptable for future enhancements.

- **Data Extraction**: Utilizes Selenium and Requests for web scraping, Beautiful Soup for HTML parsing, and preprocessing techniques for data cleaning and transformation.

- **ETL Operations**: Implements the Extract, Transform, Load (ETL) process to extract data, process it, and store it in a database for easy access and analysis.

- **Database Operations**: Data extracted by the scrapers is pushed to a database for seamless storage and retrieval.

- **Output Management**: Organizes output data into CSV files stored in an `output` folder for convenient access and analysis.

- **Threading for Performance**: Utilizes threads to enhance scraping speed, optimizing the data extraction process.

- **Error Handling**: Implements multiple error-handling techniques to ensure the efficiency and robustness of the scraping process.

- **Integration**: The project aims to integrate data from various sources into a unified platform, simplifying the user experience and providing comprehensive tech offer information.

## Note

In the `LaptopDB` class, users are required to provide or set the database name, username, and password for their database connection:

```python
conn = psycopg2.connect(database = "<Database Name>",
                        user = "<include your username>",
                        password = "<include your password>",
                        host = "127.0.0.1",
                        port = "5432")

engine = create_engine('postgresql://<username>:<password>@localhost:5432/<DB name>')

```

## Instructions

- **Install Packages**: Run the following command to install required packages:

```python
pip install -r requirements.txt
```

- **Run Main File**: Execute the following command to run the main file:

```python
python -m tech_scraper_main.py
```

---
