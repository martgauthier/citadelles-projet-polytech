package fr.cotedazur.univ.polytech.citadellesgroupeq.evaluator;

import fr.cotedazur.univ.polytech.citadellesgroupeq.playerevaluator.CSVLineIndexes;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CSVLineIndexesTest {
    @Test
    void testValues() {
        int[] values=new int[] {0,1,2,3,4,5,6,7};
        int[] ordinals=Arrays.asList(CSVLineIndexes.values()).stream().mapToInt(enumValue -> enumValue.ordinal()).toArray();
        for(int index=0; index<values.length; index++) {
            assertEquals(values[index], ordinals[index]);
        }
    }
}
