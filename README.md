StundenExcelWriterPlugin
=======================

Plugin for https://github.com/sne11ius/stunden that fills out a template Excel file.

Configuration
=============

Unfortunately, quite a bunch of settings. Indexes are zero based.

`startTime` the time to start each simplified day with - in format `hh:mm'.

`templateExcelFilename`: Path to file to use as template. Example: `"C:/Users/PowPow/Desktop/Excel_template.xlsx"`

`outputExcelFilename`: Path to write the resulting excel file to. Example: `"C:/Users/PowPow/Desktop/Excel_current.xlsx"`

`tags`: A list of tags. This plugin will only consider entries that are tagged by at least on tag from this list
(See https://github.com/sne11ius/StundenTagEntriesPlugin). Example: `["tag1", "tag2"]`

`travelArrivalIndicators`: A list of strings indicating that this entry is a travel _to_ somewhere.
they will be cross checked with the title of entries. Example: `["Reise nach", "Fahrt nach"]`

`travelDepartureIndicators`: Just like `travelArrivalIndicators`, but indicating that you travelled _from_
somewhere. Example: `["Bremen", "Rückfahrt", "Heimfahrt"]`

`autoBreakBonus`: Number of minutes to add to the total work duration for each day (just in case
your Excel template has some weird "auto break" feature... ). Example: `30`

`startRow`: First row to enter data into. Each day will use one row. Example: `8`

`dateCol`: The column to write the date to. Example: `1`

`startWorkCol`: The column to write the start time to. Example: `6`

`endWorkCol`: The column to write the end time to. Example: `7`

`startArrivalCol`: The column to write the start of a travel _to_ somewhere to. Example: `4`

`endArrivalCol`: See above.

`startDepartureCol`: The column to write the start of a travel _from_ somewhere to. Example: `8`

`endDepartureCol`: See above.

`descriptionCol`: The column to write a combined description to. The description will
be combined from all relevant entries for a given day. Example: `11`

Build
=====
see https://github.com/sne11ius/stunden
