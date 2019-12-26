# simplePeopleBase

The application stores the customer data in the H2 database.

Tables:
- Customer
- Contact

## Importing

It's possible to import customer data from the XML/CSV file into database.

> **NOTE:** Input files are not fully loaded into memory - they are partially loaded!

In the xml parser *javax.xml.stream* is used. The parsers implements *Enumeration* interface to provide friendly use
