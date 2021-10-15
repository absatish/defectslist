package com.defectlist.inwarranty;

import com.defectlist.inwarranty.model.GridItem;
import org.springframework.stereotype.Component;

@Component
public class GridItemFactory {

    private static final String DELIMITER_NEW_LINE = "\n";

    public GridItem buildGridItem(final String complainId, final String spareName, final String htmlData) {
        final String[] lines = htmlData.split(DELIMITER_NEW_LINE);
        final String date = "20" + complainId.substring(1, 3) + "-" + complainId.substring(3,5) + "-" + complainId.substring(5, 7);
        final GridItem gridItem = new GridItem();
        int lineNumber = 0;
        for(final String line : lines) {
            final String modifiedLine = line
                    .replaceAll("<font size=\"1\">", "")
                    .replaceAll("\\t", "")
                    .replaceAll("</font>\\r", "");
            switch (lineNumber) {
                case 194:
                    gridItem.setProduct(modifiedLine);
                    break;
                case 208:
                    gridItem.setModel(modifiedLine);
                    break;
                case 222:
                    gridItem.setSerialNumber(modifiedLine);
                    break;
                case 236:
                    gridItem.setDop(modifiedLine);
            }
            lineNumber++;
        }
        gridItem.setBranchName("ELURU");
        gridItem.setActualFault("Shortage");
        gridItem.setComplaintNumber(complainId);
        gridItem.setTechName("VIKRAM SIVA KUMAR");
        gridItem.setDate(date);
        gridItem.setSpareName(spareName);
        return gridItem;
    }
}
