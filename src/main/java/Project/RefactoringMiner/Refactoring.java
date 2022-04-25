package Project.RefactoringMiner;

import lombok.Data;

import java.util.List;

@Data
public class Refactoring {
    private String type;
    private String description;
    private List<SideLocation> leftSideLocations;
    private List<SideLocation> rightSideLocations;

    private String methodSignature;

}
