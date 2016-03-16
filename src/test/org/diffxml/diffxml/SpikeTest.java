package org.diffxml.diffxml;

import org.diffxml.DiffXmlUtils;
import org.diffxml.patchxml.DULPatch;
import org.diffxml.patchxml.PatchFormatException;
import org.diffxml.patchxml.PatchXML;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SpikeTest extends DiffXmlUtils {
    //----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------
    CommonContext commonContext = new CommonContext();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------------------------
// Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void test_diff_a_and_a_1() throws Exception {

        Diff diff = DiffFactory.createDiff();

        File fileA = getFile("./adf_a.xml");
        Document delta = diff.diff(fileA, getFile("./adf_a_1.xml"));
        System.out.println("/----------DELTA----------/");
        System.out.println(getString(delta));
        Document documentA = getDocument(fileA);
        PatchXML.debug = false;
        new DULPatch(commonContext).apply(documentA, delta);
        System.out.println("/----------NEW A----------/");
        System.out.println(getString(documentA));
        xpath.setNamespaceContext(commonContext);
        assertThat(((Node) xpath.compile("//activity:activity/activity:sequence[1]/activity:container[2]").evaluate(documentA, XPathConstants.NODE)).getAttributes().getNamedItem("id").getNodeValue(), is("CONTAINER A1"));
        assertThat(xpath.compile("//activity:activity/activity:sequence[1]/activity:container[@id='CONTAINER A1']/activity:component-definition/common:asset-reference/common:asset-type").evaluate(documentA), is("activity"));
        assertThat(xpath.compile("//activity:activity/activity:sequence[1]/activity:container[@id='CONTAINER A1']/activity:component-definition/common:asset-reference/common:reference-id").evaluate(documentA), is("1"));
    }


    /*
     Create A
     Create A1 and B from A
     Edit A1                            (added container A1)
     Edit B                             (added container B)
     Create B1 from B
     Edit B1                            (added container B1)
     Patch B with A1                    (patched with container A1)
     Patch B1 with A1                   (patched with container A1)
    */
    @Test
    public void test_patch_on_forked_files() throws Exception {
        Diff diff = DiffFactory.createDiff();

        File fileA = getFile("adf_a.xml");
        File fileA1 = getFile("adf_a_1.xml");
        File fileB = getFile("adf_b.xml");
        File fileB1 = getFile("adf_b_1.xml");

        Document deltaA = diff.diff(fileA, fileA1);
        Document documentB = getDocument(fileB);
        Document documentB1 = getDocument(fileB1);

        System.out.println("/----------DELTA----------/");
        System.out.println(getString(deltaA));

        new DULPatch(commonContext).apply(documentB1, deltaA);
        new DULPatch(commonContext).apply(documentB, deltaA);


        System.out.println("/----------NEW B----------/");
        System.out.println(getString(documentB));
        System.out.println("/----------NEW B1----------/");
        System.out.println(getString(documentB1));

    }

    @Test
    public void test_patch_with_a_change_in_the_middle() throws Exception {

        File fileB = getFile("adf_b.xml");
        File fileB1 = getFile("adf_b_1.xml");

        Document deltaA = getDocument(getFile("delta.xml"));
        Document documentB = getDocument(fileB);
        Document documentB1 = getDocument(fileB1);

        System.out.println("/----------DELTA----------/");
        System.out.println(getString(deltaA));

        new DULPatch(commonContext).apply(documentB1, deltaA);
        new DULPatch(commonContext).apply(documentB, deltaA);


        System.out.println("/----------NEW B----------/");
        System.out.println(getString(documentB));
        System.out.println("/----------NEW B1----------/");
        System.out.println(getString(documentB1));

    }
}
