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

![UML Class Diagram](images/uml-diagram-1.2.1.png)

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
- PostgreSQL database with two tables :
    - wine(wno serial, name varchar, year int, volume numeric, color varchar, price numeric, comment varchar, #ano int)
    - assortment(ano serial, year int)

## Getting Started

the software is available on every operating system

### Windows

To install the software download and launch the .exe or .msi file from the release page and to use it,
 double-click on the shortcut on the desktop if you selected create shortcut in the installer

### Mac

To install the software download and launch the .dmg or .pkg file from the release page and to use it,
click on the icon the application menu

### Linux

To install the software,
download the latest WineStock-ubuntu-latest.jar in the release page and double-click the jar archive to launch it

## Usage

Upon running the application, you will be presented with a user interface containing tabs for managing wine and
assortment data.
To import wine data from the database, click the database url and credentials, then click login.
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
- Apache Log4j 2.14.1: A Java-based logging utility that provides logging capabilities through a configuration file.
- AtlantaFX 1.0.0: A JavaFX theme that provides a modern, flat, and clean look and feel for JavaFX applications.

## Contributing

Contributions to the WineStock application are welcome. If you find any issues or have suggestions for improvement,
please submit an issue or create a pull request.

## License

This project is licensed under the [MIT License](../LICENSE).

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/summary/new_code?id=Monodia3007_WineStock)
