# WineStock Application
The WineStock application is a JavaFX-based application for managing wine inventory and assortment data. It provides functionality to connect to a PostgreSQL database, retrieve wine information, and perform various operations on the data.

## Features
- View wine details including name, vintage year, volume, color, price, and comment.
- Import wine data from a PostgreSQL database.
- Login functionality to establish a connection to the database.
- Manage wine assortment data.

## Prerequisites
- Java Development Kit (JDK) 17 or higher
- PostgreSQL database

## Getting Started
To run the WineStock application, you can follow these steps:
1. Download the latest release from the "Releases" section of the repository.
2. Extract the contents of the release package.
3. Open a terminal or command prompt.
4. Navigate to the directory where the extracted files are located.
5. Run the following command to execute the application:
```bash
java -jar WineStock.jar
```
6. Replace WineStock.jar with the actual name of the JAR file if it's different.

The WineStock application should start, and you will be able to interact with it through the graphical user interface.

Make sure you have Java Runtime Environment (JRE) 17 or higher installed on your system. If not, you can download and install it from the official Oracle website.

Note: The instructions assume that you have a valid database with the necessary credentials and configuration file (`config.json`) in the same directory as the JAR file. Make sure to set up the database and provide the correct credentials in the configuration file before running the application.

If you encounter any issues or need further assistance, please let me know.

## Usage
Upon running the application, you will be presented with a user interface containing tabs for managing wine and assortment data. To import wine data from the database, click the "Login" button and enter your database credentials. Once logged in, click the "Import Database" button to fetch the wine data and display it in the table.

You can switch between tabs to perform various operations on the wine and assortment data.

## Dependencies
- PostgreSQL JDBC Driver 42.6.0
- JSON Library 20230618
- JUnit Jupiter API (for testing) 5.9.2

## Contributing
Contributions to the WineStock application are welcome. If you find any issues or have suggestions for improvement, please submit an issue or create a pull request.

## License
This project is licensed under the [MIT License](./LICENSE).