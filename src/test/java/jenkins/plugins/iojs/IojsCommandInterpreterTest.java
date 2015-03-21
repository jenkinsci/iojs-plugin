package jenkins.plugins.iojs;

import hudson.FilePath;
import hudson.model.Descriptor;
import jenkins.plugins.iojs.tools.IojsInstallation;
import hudson.tasks.Builder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.util.Collections;

import static de.regnis.q.sequence.core.QSequenceAssert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class IojsCommandInterpreterTest {

    private static final String COMMAND = "var sys = require('sys'); sys.puts('build number: ' + process.env['BUILD_NUMBER']);";

    private IojsCommandInterpreter interpreter;
    private Descriptor<Builder> descriptor;
    private IojsInstallation installation;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() {
        installation = new IojsInstallation("1.6.1", "", Collections.EMPTY_LIST);
        interpreter = new IojsCommandInterpreter(COMMAND, installation.getName());
        descriptor = interpreter.getDescriptor();
    }

    @Test
    public void testGetContentsShouldGiveExpectedValue() {
        assertEquals(COMMAND, interpreter.getCommand());
    }

    @Test
    public void testGetContentWithEmptyCommandShouldGiveExpectedValue() {
        assertEquals("", new IojsCommandInterpreter("", installation.getName()).getCommand());
    }

    @Test
    public void testGetContentWithNullCommandShouldGiveExpectedValue() {
        assertNull(new IojsCommandInterpreter(null, installation.getName()).getCommand());
    }

    @Test
    public void testGetFileExtensionShouldGiveExpectedValue() throws IOException, InterruptedException {
        assertEquals(true, interpreter.createScriptFile(new FilePath(tempFolder.newFolder())).getName().endsWith(".js"));
    }

    @Test
    public void testGetDescriptorShouldGiveExpectedValue() {
        assertNotNull(descriptor);
        assertTrue(descriptor instanceof Descriptor<?>);
    }

    @Test
    public void testDescriptorGetDisplayNameShouldGiveExpectedValue() {
        assertEquals("Execute io.js script", descriptor.getDisplayName());
    }

    @Test
    public void testDescriptorGetHelpFileShouldGiveExpectedValue() {
        assertEquals("/plugin/iojs/help.html", descriptor.getHelpFile());
    }
}
