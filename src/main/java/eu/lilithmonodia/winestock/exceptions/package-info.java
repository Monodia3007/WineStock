/**
 * The package 'eu.lilithmonodia.winestock.exceptions' includes custom exceptions
 * tailored to handle issues that may arise during the usage of the WineStock application.
 * <p>
 * It includes exceptions such as:
 * <ul>
 *     <li><b>InvalidYearException:</b> Thrown when the year provided for a wine is invalid.
 *     (E.g., year is in the future or far in the past)</li>
 *     <li><b>InvalidBottleVolumeException:</b> Thrown if the bottle size volume does not
 *     match any volume in the BottleSize enum.</li>
 *     <li><b>InvalidWineColorException:</b> Thrown if the set wine color does not align
 *     with any color in the Color enum.</li>
 *     <li><b>WineAlreadyInAssortmentException:</b> Thrown to prevent a wine already present
 *     in an assortment from being added again.</li>
 *     <li><b>WineNotInAssortmentException:</b> Thrown if an attempt is made to remove a wine
 *     from an assortment, but the wine is not present in the assortment.</li>
 * </ul>
 */
package eu.lilithmonodia.winestock.exceptions;