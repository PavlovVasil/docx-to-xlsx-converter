# docx-to-xlsx-converter
A small java desktop app that converts an MS Word ".docx" text file to an MS Excel ".xlsx" table.

If you try to copy a large text into MS Excel, it doesn't spread vertically across the rows. So you have to manually copy - paste 
every bit of text to create the table.

This app is doing the dirty work for you and saves you some time. It parses the ".docx" text document and creates an MS Excel table with a new row for every paragraph. 
It removes the empty cells (if any) and the user has an option to limit the number of symbols it puts in each cell. 
Note that the app works ONLY for the new MS formats ".docx" and ".xlsx".
