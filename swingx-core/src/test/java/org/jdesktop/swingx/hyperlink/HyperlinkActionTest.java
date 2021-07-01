/*
 * Created on 28.03.2006
 *
 */
package org.jdesktop.swingx.hyperlink;

import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.swing.Action;

import org.jdesktop.test.PropertyChangeReport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import junit.framework.TestCase;


/**
 * 
 * @author Jeanette Winzenburg, Berlin
 */
@RunWith(JUnit4.class)
public class HyperlinkActionTest extends TestCase {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(HyperlinkActionTest.class.getName());
    
    private PropertyChangeReport report;

    @Before
    public void setUpJ4() throws Exception {
        setUp();
    }
    
    @After
    public void tearDownJ4() throws Exception {
        tearDown();
    }
    
    /**
     * Issue #1227-swingx: HyperlinkAction - update visited property
     * @throws URISyntaxException
     */
    @Test
    public void testVisited() throws URISyntaxException {
        assumeFalse("cannot run ui test - headless environment", GraphicsEnvironment.isHeadless());
        assumeTrue("Desktop not supported, therefore Desktop-dependent actions can't be tested.", Desktop.isDesktopSupported());
        assumeTrue("Required Desktop-Action not supported, therefore test this action can't be tested.", Desktop.getDesktop().isSupported(Desktop.Action.BROWSE));

        URI uri = new URI("http://127.0.0.1");
        HyperlinkAction action = HyperlinkAction.createHyperlinkAction(uri);
        action.actionPerformed(null);
        assertEquals(true, action.isVisited());
    }
    
    @Test
    public void testURIActionFactoryMail() throws URISyntaxException {
        assumeFalse("cannot run ui test - headless environment", GraphicsEnvironment.isHeadless());
        assumeTrue("Desktop not supported, therefore Desktop-dependent actions can't be tested.", Desktop.isDesktopSupported());
        assumeTrue("Required Desktop-Action not supported, therefore test this action can't be tested.", Desktop.getDesktop().isSupported(Desktop.Action.MAIL));
        
        URI uri = new URI("mailto:java-net@java.sun.com");
        HyperlinkAction action = HyperlinkAction.createHyperlinkAction(uri);
        assertEquals(true, action.isEnabled());
        assertEquals(Desktop.Action.MAIL, action.getDesktopAction());
    }
    
    @Test
    public void testURIActionFactoryBrowseNull() {
        assumeFalse("cannot run ui test - headless environment", GraphicsEnvironment.isHeadless());
        
        HyperlinkAction action = HyperlinkAction.createHyperlinkAction(null);
        assertEquals(false, action.isEnabled());
        assertEquals(Desktop.Action.BROWSE, action.getDesktopAction());
    }
    
    @Test
    public void testURIActionPerformed() {
        assumeFalse("cannot run ui test - headless environment", GraphicsEnvironment.isHeadless());
        
        HyperlinkAction action = new HyperlinkAction();
        action.actionPerformed(null);
    }
    
    @Test
    public void testURIActionNullMailEnabled() {
        assumeFalse("cannot run ui test - headless environment", GraphicsEnvironment.isHeadless());
        assumeTrue("Desktop not supported, therefore Desktop-dependent actions can't be tested.", Desktop.isDesktopSupported());
        assumeTrue("Required Desktop-Action not supported, therefore test this action can't be tested.", Desktop.getDesktop().isSupported(Desktop.Action.MAIL));

        HyperlinkAction action = new HyperlinkAction(Desktop.Action.MAIL);
        assertEquals(true, action.isEnabled());
    }
    
    @Test
    public void testURIActionNullTargetBrowseDisabled() {
        assumeFalse("cannot run ui test - headless environment", GraphicsEnvironment.isHeadless());
        
        HyperlinkAction action = new HyperlinkAction();
        assertEquals(false, action.isEnabled());
    }
    
    @Test
    public void testUriActionEmptyConstructor() {
        assumeFalse("cannot run ui test - headless environment", GraphicsEnvironment.isHeadless());

        HyperlinkAction action = new HyperlinkAction();
        assertEquals(Desktop.Action.BROWSE, action.getDesktopAction());
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void testUriActionIllegalType() {
        assumeFalse("cannot run ui test - headless environment", GraphicsEnvironment.isHeadless());

        new HyperlinkAction(Desktop.Action.EDIT);
    }

    /**
     * test if auto-installed visited property is respected.
     *
     */
    @Test
    public void testConstructorsAndCustomTargetInstall() {
        Object target = new Object();
        final boolean visitedIsTrue = true;
        AbstractHyperlinkAction<Object> linkAction = new AbstractHyperlinkAction<Object>(target) {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void installTarget() {
                super.installTarget();
                setVisited(visitedIsTrue);
            }
            
            
            
        };
        assertEquals(visitedIsTrue, linkAction.isVisited());
        
    }
    /**
     * test constructors with parameters
     *
     */
    @Test
    public void testConstructors() {
        Object target = new Object();
        AbstractHyperlinkAction<Object> linkAction = new AbstractHyperlinkAction<Object>(target) {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        };
        assertEquals(target, linkAction.getTarget());
        assertFalse(linkAction.isVisited());
    }
    /**
     * test visited/target properties of LinkAction.
     *
     */
    @Test
    public void testLinkAction() {
       AbstractHyperlinkAction<Object> linkAction = new AbstractHyperlinkAction<Object>(null) {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            
        }
           
       };
       linkAction.addPropertyChangeListener(report);
       
       boolean visited = linkAction.isVisited();
       assertFalse(visited);
       linkAction.setVisited(!visited);
       assertEquals(!visited, linkAction.isVisited());
       assertEquals(1, report.getEventCount(AbstractHyperlinkAction.VISITED_KEY));
       
       report.clear();
       // testing target property
       assertNull(linkAction.getTarget());
       Object target = new Object();
       linkAction.setTarget(target);
       assertEquals(target, linkAction.getTarget());
       assertEquals(1, report.getEventCount("target"));
       // testing documented default side-effects of un/installTarget
       assertEquals(target.toString(), linkAction.getName());
       assertFalse(linkAction.isVisited());
       assertEquals(1, report.getEventCount(Action.NAME));
       assertEquals(1, report.getEventCount(AbstractHyperlinkAction.VISITED_KEY));
       // fired the expected events only.
       assertEquals(3, report.getEventCount());
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        report = new PropertyChangeReport();
    }

    
}
