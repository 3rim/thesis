package com.erim.bachelor.helper;

import com.erim.bachelor.entities.Borrower;
import org.apache.commons.csv.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVHelper {

    public static String TYPE = "text/csv";
    static String[] HEADERS = { "Id", "firstName", "lastName" };

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static ByteArrayInputStream usersToCSV(List<Borrower> borrowers){
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream),format)) {
            for (Borrower borrower : borrowers){
                //Skip borrower if he has no BorrowerNr
                if(borrower.getBorrowerNr() == null)
                    continue;
                List<String> data = Arrays.asList(
                        String.valueOf(borrower.getBorrowerNr()),
                        String.valueOf(borrower.getFirstName()),
                        String.valueOf(borrower.getLastName())
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
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Borrower> borrowers = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Borrower borrower = new Borrower(
                        Long.parseLong(csvRecord.get("Id")),
                        csvRecord.get("firstName"),
                        csvRecord.get("lastName")
                );

                borrowers.add(borrower);
            }

            return borrowers;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
