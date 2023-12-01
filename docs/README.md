# WineStock Application

| [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Monodia3007_WineStock&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=Monodia3007_WineStock)          | [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Monodia3007_WineStock&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Monodia3007_WineStock)            | [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Monodia3007_WineStock&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=Monodia3007_WineStock)        | [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Monodia3007_WineStock&metric=bugs)](https://sonarcloud.io/summary/new_code?id=Monodia3007_WineStock)                         |
|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Monodia3007_WineStock&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Monodia3007_WineStock)  | [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=Monodia3007_WineStock&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=Monodia3007_WineStock)  | [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Monodia3007_WineStock&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Monodia3007_WineStock)  | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Monodia3007_WineStock&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Monodia3007_WineStock)  |
| [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=Monodia3007_WineStock&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=Monodia3007_WineStock)       | [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Monodia3007_WineStock&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Monodia3007_WineStock)                              | [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=Monodia3007_WineStock&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=Monodia3007_WineStock)                    |

The WineStock application is a JavaFX-based application for managing wine inventory and assortment data. It provides
functionality to connect to a PostgreSQL database, retrieve wine information, and perform various operations on the
wine and assortment data.

## Table of Contents

- [WineStock Application](#winestock-application)
    - [Table of Contents](#table-of-contents)
    - [UML Diagram](#uml-diagram)
    - [Description](#description)
    - [Features](#features)
    - [Prerequisites](#prerequisites)
    - [Getting Started](#getting-started)
    - [Usage](#usage)
    - [Dependencies](#dependencies)
    - [Contributing](#contributing)
    - [License](#license)

## UML Diagram

Here is the UML class diagram of the application:

![UML Class Diagram](images/uml-diagram-1.1.1.png)

## Description

The WineStock application is a JavaFX-based application for managing wine inventory and assortment data. It provides
functionality to connect to a PostgreSQL database, retrieve wine information, and perform various operations on the
wine and assortment data.

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
    ```shell
    java -jar WineStock.jar
    ```
6. Replace WineStock.jar with the actual name of the JAR file if it's different.

The WineStock application should start, and you will be able to interact with it through the graphical user interface.

Make sure you have Java Runtime Environment (JRE) 17 or higher installed on your system. If not, you can download and
install it from the official Oracle website.

Note: The instructions assume that you have a valid database with the necessary credentials and configuration
file (`config.json`) in the same directory as the JAR file. Make sure to set up the database and provide the correct
credentials in the configuration file before running the application.

If you encounter any issues or need further assistance, please let me know.

## Usage

Upon running the application, you will be presented with a user interface containing tabs for managing wine and
assortment data. To import wine data from the database, click the "Login" button and enter your database credentials.
Once logged in, click the "Import Database" button to fetch the wine data and display it in the table.

You can switch between tabs to perform various operations on the wine and assortment data.

## Dependencies

This project uses the following dependencies:

- **JavaFX 17.0.7**: An open-source, next-generation client application platform for desktop, mobile and embedded
  systems.
- **PostgreSQL JDBC Driver 42.6.0**: A JDBC (Java Database Connectivity) driver that provides database connectivity
  through the standard JDBC application program interfaces (APIs) available in Java.
- **JetBrains Annotations 24.0.0**: An advanced set of annotations used for code inspection support and code
  documentation.
- **Google Gson 2.8.9**: A Java library that can be used to convert Java Objects into their JSON representation and vice
  versa.
- **Apache Commons IO 2.11.0**: Provides utility classes, stream implementations, file filters, and various other APIs
  for handling IO operations (file manipulations) in Java.

## Contributing

Contributions to the WineStock application are welcome. If you find any issues or have suggestions for improvement,
please submit an issue or create a pull request.

## License

This project is licensed under the [MIT License](../LICENSE).

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/summary/new_code?id=Monodia3007_WineStock)
