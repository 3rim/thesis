package com.erim.bachelor.helper;

import com.erim.bachelor.entities.Borrower;
import org.apache.commons.csv.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class CSVHelper {
    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static ByteArrayInputStream usersToCSV(List<Borrower> borrowers){
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream),format)) {
            for (Borrower borrower : borrowers){
                //Skip borrower if he has no BorrowerNr
                if(borrower.getBorrowerNr() == null)
                    continue;

                LocalDate dateTime = borrower.getDob();

                // using short german date/time formatting (01.04.14 10:45)
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMAN);
                String germanDate = dateTime.format(formatter);

                List<String> data = Arrays.asList(
                        String.valueOf(borrower.getBorrowerNr()),
                        String.valueOf(borrower.getFirstName()),
                        String.valueOf(borrower.getLastName()),
                        String.valueOf(borrower.getBorrowerGroup()),
                        germanDate,
                        String.valueOf(borrower.getEmail())
                );

                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
        catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }

    public static List<Borrower> csvToUsers(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            char delimiter;
            String header = fileReader.readLine();
            if(header.indexOf(';') != -1)
                delimiter = ';';
            else
                delimiter = ',';

            CSVParser csvParser = new CSVParser(fileReader,
                    CSVFormat.DEFAULT.withDelimiter(delimiter).withTrim());
            List<Borrower> borrowers = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            int indexID ,indexFirstName,indexLastName,indexGroup,indexDateOfBirth,indexEmail;
            indexID=0;
            indexFirstName=1;
            indexLastName=2;
            indexGroup=3;
            indexDateOfBirth=4;
            indexEmail=5;

            for (CSVRecord csvRecord : csvRecords) {
                String csvDOB = csvRecord.get(indexDateOfBirth);
                /*
                 * LocalDate standard is: yyyy-MM-dd. Read german format and parse it to the ISO-Standard
                 * The string must represent a valid date in the format of ISO LocalDate
                 */
                DateTimeFormatter germanFormatter = DateTimeFormatter.ofLocalizedDate(
                        FormatStyle.MEDIUM).withLocale(Locale.GERMAN);
                LocalDate dob = LocalDate.parse(csvDOB, germanFormatter);

                Borrower borrower = new Borrower(
                        Long.parseLong(csvRecord.get(indexID)),
                        csvRecord.get(indexFirstName),
                        csvRecord.get(indexLastName),
                        csvRecord.get(indexGroup),
                        dob,
                        csvRecord.get(indexEmail)
                );

                borrowers.add(borrower);
            }

            return borrowers;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
