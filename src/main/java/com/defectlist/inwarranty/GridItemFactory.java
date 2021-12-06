package com.defectlist.inwarranty;

import com.defectlist.inwarranty.model.GridItem;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class GridItemFactory {

    private static final String DELIMITER_NEW_LINE = "\n";

    public GridItem buildGridItem(final String complainId, final String spareName, final String htmlData,
                                  final String loggedInUserName) {
        final String[] lines = htmlData.split(DELIMITER_NEW_LINE);
        final String date = "20" + complainId.substring(1, 3) + "-" + complainId.substring(3,5) + "-" + complainId.substring(5, 7);
        final GridItem.GridItemBuilder gridItemBuilder = GridItem.builder();
        Stream.iterate(0, lineNumber -> ++lineNumber)
                .limit(lines.length)
                .forEach(lineNumber -> buildRequiredInfo(lines[lineNumber], lineNumber, gridItemBuilder));
        gridItemBuilder.branchName("ELURU");
        gridItemBuilder.actualFault("Shortage");
        gridItemBuilder.complaintNumber(complainId);
        gridItemBuilder.techName(loggedInUserName);
        gridItemBuilder.date(date);
        gridItemBuilder.spareName(spareName);
        return gridItemBuilder.build();
    }

    private void buildRequiredInfo(final String line, final int lineNumber, final GridItem.GridItemBuilder gridItemBuilder) {
        final String modifiedLine = line
                .replaceAll("<font size=\"1\">", "")
                .replaceAll("\\t", "")
                .replaceAll("</font>\\r", "");
        switch (lineNumber) {
            case 194:
                gridItemBuilder.product(modifiedLine);
                break;
            case 208:
                gridItemBuilder.model(modifiedLine);
                break;
            case 222:
                gridItemBuilder.serialNumber(modifiedLine);
                break;
            case 236:
                gridItemBuilder.dop(modifiedLine);
        }
    }
}
