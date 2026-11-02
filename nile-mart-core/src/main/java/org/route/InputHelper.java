package org.route;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputHelper {
    private final Scanner input=new Scanner(System.in);

    public int readInt(String msg){
        while(true){
            try{
                System.out.print(msg);
                return Integer.parseInt(input.nextLine());
            }catch (NumberFormatException e){
                System.out.println("Please enter a valid number.");
            }
        }
    }
    public double readDouble(String msg){
        while(true){
            try{
                System.out.print(msg);
                double value = Double.parseDouble(input.nextLine());
                if(!Double.isFinite(value)){
                    System.out.println("Please enter a valid finite number.");
                    continue;
                }
                return value;
            }catch (NumberFormatException e){
                System.out.println("Please enter a valid number.");
            }
        }
    }
    public String readString(String msg){
        System.out.print(msg);
        return input.nextLine();
    }
    public BigDecimal readBigDecimal(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                BigDecimal value = new BigDecimal(input.nextLine());

                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    public Double readOptionalDouble(String msg) {
        while (true) {
            String input = readString(msg);

            if (input.isBlank()) {
                return null;
            }

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public BigDecimal readOptionalBigDecimal(String msg) {
        while (true) {
            String input = readString(msg);

            if (input.isBlank()) {
                return null;
            }

            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid price.");
            }
        }
    }
    public LocalDate readLocalDate(String message) {
        while (true) {
            String input = readString(message);

            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Please use YYYY-MM-DD.");
            }
        }
    }
    public boolean readYesNo(String message) {
        int choice;

        do {
            System.out.println(message);
            System.out.println("1. Yes");
            System.out.println("2. No");

            choice = readInt("Choose: ");

            if (choice < 1 || choice > 2) {
                System.out.println("Invalid choice.");
            }
        } while (choice < 1 || choice > 2);

        return choice == 1;
    }
}