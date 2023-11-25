# Exceptions Package
The eu.lilithmonodia.winestock.exceptions package contains custom exception classes designed to handle specific issues that may come up during the use of the WineStock application.

## Overview
The package includes the following exceptions:
- InvalidYearException: This is thrown when the provided year for a wine is invalid. Examples of invalid years would be years in the future or far in the past.
- InvalidBottleVolumeException: This exception is thrown when the bottle size volume doesn't match any volume in the BottleSize enum.
- WineAlreadyInAssortmentException: This exception is raised to prevent a wine that is already included in an assortment from being added again.
- WineNotInAssortmentException: This gets thrown when there is an attempt to remove a wine from an assortment, but the wine is not present in that assortment.

```java
package eu.lilithmonodia.winestock.exceptions;
```

To sum up, the eu.lilithmonodia.winestock.exceptions package has a pivotal role in error handling within the WineStock application. These custom exceptions enable more granular and specific error reporting, which can improve debugging and the overall robustness of the application.