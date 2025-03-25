/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import domain.Booking;
import domain.Price;

import dbtools.PriceSQLProvider;

/**
 *
 * @author iyana
 */
public class InvoiceGenerator {
    public static double calculateTotalAmount(List<Booking> bookings) {
        double totalAmount = 0.0;
        PriceSQLProvider priceProvider = new PriceSQLProvider();

        for (Booking booking : bookings) {
            // Get the latest price for this equipment based on delivery date
            Price price = priceProvider.getLatestPrice(booking.getEquipmentID(), booking.getDeliveryDate());
            
            if (price != null) {
                double ratePerHour = price.getRatePerHour();

                // Calculate rental duration in hours
                long hoursRented = ChronoUnit.HOURS.between(
                        booking.getDeliveryDate().atStartOfDay(),
                        booking.getReturnDate().atStartOfDay()
                );

                // Prevent negative hours (if return date is the same day)
                if (hoursRented == 0) {
                    hoursRented = 1; // Assume at least 1 hour of rental
                }

                // Multiply ratePerHour by hours rented
                double cost = ratePerHour * hoursRented;
                totalAmount += cost;
            }
        }

        return totalAmount;
    }  
}

/*
A utility class is a class that contains only static methods and is not meant to be instantiated. 
It is used for reusable helper functions that don’t belong to any one specific object.

For example:
Math in Java (Math.pow(), Math.sqrt())
Collections (Collections.sort())

Since invoice calculation is a reusable operation, it makes sense to put it in a separate 
InvoiceGenerator utility class instead of the Invoice domain class.


*/
