# Top Laptops and Deals Website

This Spring Boot Java web project aims to provide users with a centralized platform to view top-tier laptops, explore different websites top deals and offers, and access detailed specifications and deal links for each laptop. The project utilizes a Python web scraper to collect data from various websites, enabling seamless integration of laptop data into a single database.

## Project Aims and Description

The primary goal of this project is to simplify the process of finding the best laptop deals by aggregating and collecting top-tier laptops and offers from various sources into a single platform. Users can effortlessly browse through a curated list of laptops, apply filters based on categories and budgets, and access detailed specifications along with direct links to available deals. The project aims to enhance user experience, save time, and provide a comprehensive solution for accessing laptop information and offers conveniently.

## Future Works

In the future, the project can be scaled to include a broader range of tech devices beyond laptops. By expanding the platform to cover different categories of tech products, users will have access to a wider selection of devices and offers, further enhancing the platform's utility and value.

## Key Features

- View a curated list of top-tier laptops
- Explore available offers from scraped websites
- Search for laptops by model name
- View user authentication info and contribution status
- Access detailed laptop specifications
- Apply filters to refine laptop search results
- Efficiently add, update, and delete laptops

## Authentication and Roles

The project implements multiple levels of authorization, including user, admin, and anonymous roles. Users can have combined roles with unique authorities and features tailored to their needs.

## Technologies Used

### Backend

- **Spring Boot**: Java-based framework for building robust, production-ready applications.

### Frontend

- **CSS, HTML, JavaScript, jQuery, Bootstrap**: Frontend technologies for creating responsive and interactive user interfaces.

### Python Web Scraper

- **Selenium**: Web automation tool for browser automation and testing.

- **Requests**: HTTP library for making HTTP requests in Python.

- **BeautifulSoup**: HTML parsing library for extracting data from HTML and XML files.

- **Pandas**: Data manipulation and analysis library for Python.


## Pages

1. **Home**: Display all laptops and top deals, with a section for admins that allows them to manage latest laptops data.

2. **Top Deals**: Showcase available laptop offers with filter options.

3. **Search**: Search for laptops by model name.

4. **Contributions**: Show user info and contribution status, including user's list of added laptops.

5. **Add New Laptop**: Add a new laptop with specifications.
