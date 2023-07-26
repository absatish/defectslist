package com.defectlist.inwarranty;

import com.defectlist.inwarranty.model.GridItem;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class GridItemFactory {

    private static final String DELIMITER_NEW_LINE = "\n";
    private static final String PRODUCT = "Product";
    private static final String SERIAL_NUMBER = "Serial No";
    private static final String MODEL = "Model";
    private static final String DOP = "DOP";


    public GridItem buildGridItem(final String complainId, final String spareName, final String htmlData,
                                  final String loggedInUserName) {
        final String[] lines = htmlData.split(DELIMITER_NEW_LINE);
        final String date = "20" + complainId.substring(1, 3) + "-" + complainId.substring(3,5) + "-" + complainId.substring(5, 7);
        final GridItem.GridItemBuilder gridItemBuilder = GridItem.builder();
        Stream.iterate(0, lineNumber -> ++lineNumber)
                .limit(lines.length)
                .forEach(lineNumber -> buildRequiredInfo(lines, lineNumber, gridItemBuilder));
        gridItemBuilder.branchName(loggedInUserName.contains("VIKRAM") ? "ELURU" : "");
        gridItemBuilder.actualFault("Shortage");
        gridItemBuilder.complaintNumber(complainId);
        gridItemBuilder.techName(loggedInUserName);
        gridItemBuilder.date(date);
        gridItemBuilder.spareName(spareName);
        return gridItemBuilder.build();
    }

    private void buildRequiredInfo(final String[] lines, final int lineNumber, final GridItem.GridItemBuilder gridItemBuilder) {
        final String line = lines[lineNumber];
        final String modifiedLine = modifyLine(line);
        try {
            switch (modifiedLine.trim()) {
                case PRODUCT:
                    gridItemBuilder.product(getAppropriateString(lines, lineNumber));
                    break;
                case MODEL:
                    gridItemBuilder.model(getAppropriateString(lines, lineNumber));
                    break;
                case SERIAL_NUMBER:
                    gridItemBuilder.serialNumber(getAppropriateString(lines, lineNumber));
                    break;
                case DOP:
                    gridItemBuilder.dop(getAppropriateString(lines, lineNumber));
                    break;
            }
        } catch (Exception exception) {
            switch (lineNumber) {
                case 312:
                    gridItemBuilder.product(modifiedLine);
                    break;
                case 326:
                    gridItemBuilder.model(modifiedLine);
                    break;
                case 342:
                    gridItemBuilder.serialNumber(modifiedLine);
                    break;
                case 356:
                    gridItemBuilder.dop(modifiedLine);
            }
        }
    }

    private String modifyLine(String line) {
        return line
            .replaceAll("<font size=\"1\">", "")
            .replaceAll("\\t", "")
            .replaceAll("</font>\\r", "");
    }

    private String getAppropriateString(String[] lines, int lineNumber) {
        return modifyLine(lines[lineNumber + 3]);
    }
}
