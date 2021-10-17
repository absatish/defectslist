package com.defectlist.inwarranty.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DefectivePartType {
    MOTOR("MOTOR", 0),
    JARS("JAR", 5),
    DRUM("DRUM", 4),
    ARMATURE("ARMATURE", 1),
    BODY("BODY", 3),
    FIELD("FIELD", 2),
    OTHER("OTHER", 5);

    private final String partType;
    private final int sortOrder;

    DefectivePartType(final String partType, final int sortOrder) {
        this.partType = partType;
        this.sortOrder = sortOrder;
    }

    public String getPartType() {
        return partType;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public static DefectivePartType getPartTypeByName(final String name) {
        return Arrays.stream(DefectivePartType.values())
                .filter(type -> type.getPartType().equals(name))
                .findFirst()
                .orElse(OTHER);
    }

    public static List<DefectivePartType> getAvailablePartTypes(final boolean includeOther) {
        return Arrays.stream(DefectivePartType.values())
                .filter(type -> !type.equals(OTHER) || includeOther)
                .collect(Collectors.toList());
    }

    public boolean matches(final String line) {
        if (partType.equals(MOTOR.getPartType())) {
            return line.contains(MOTOR.getPartType()) && !line.contains("COUPLER") && !line.contains("LEG")
                    && !line.contains("CARBON") && !line.contains("BRUSH") && !line.contains("BOTTOM")
                    && !line.contains("COVER");
        } else if (partType.equals(JARS.getPartType())) {
            return line.contains(JARS.getPartType()) && !line.contains("ASSEMBLY");
        }
        return line.contains(partType);
    }
}
