package org.verapdf.pdf.regression.tests;

import org.junit.Assert;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.qa.RegressionTestingHelper;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.verapdf.pdfa.qa.ZipBackedTestCorpus.checkFlavour;

public class RegressionTestUtils {
    private static final EnumSet<PDFAFlavour> flavours = EnumSet.of(PDFAFlavour.PDFA_1_A, PDFAFlavour.PDFA_1_B,
            PDFAFlavour.PDFA_2_A, PDFAFlavour.PDFA_2_B, PDFAFlavour.PDFA_2_U,
            PDFAFlavour.PDFA_3_A, PDFAFlavour.PDFA_3_B, PDFAFlavour.PDFA_3_U,
            PDFAFlavour.PDFA_4, PDFAFlavour.PDFA_4_F, PDFAFlavour.PDFA_4_E,
            PDFAFlavour.PDFUA_1);
    private static final EnumMap<PDFAFlavour, Set<String>> filesByFlavour = new EnumMap<>(PDFAFlavour.class);

    public static void main(String[] args) {
        test();
    }

    public static void test() {
        try {
            RegressionTestingHelper helper = new RegressionTestingHelper(false);
            if (filesByFlavour.isEmpty()) {
                for (PDFAFlavour flavour : flavours) {
                    Set<String> toAdd = helper.getPdfFileNames().stream().filter(s -> checkFlavour(s, flavour)).collect(Collectors.toSet());
                    filesByFlavour.put(flavour, toAdd);
                }
            }
            List<String> failedFiles = new ArrayList<>();
            for (PDFAFlavour flavour : flavours) {
                failedFiles.addAll(helper.getFailedPolicyComplianceFiles(flavour, null, filesByFlavour.get(flavour)));
            }
            Assert.assertFalse(RegressionTestingHelper.totalFailedPolicyJobsCount(failedFiles) > 0);
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
    }
}