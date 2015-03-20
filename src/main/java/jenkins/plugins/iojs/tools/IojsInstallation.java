package jenkins.plugins.iojs.tools;

import hudson.*;
import hudson.model.EnvironmentSpecific;
import hudson.model.Node;
import hudson.model.TaskListener;
import jenkins.plugins.iojs.IojsPlugin;
import hudson.remoting.Callable;
import hudson.slaves.NodeSpecific;
import hudson.tools.ToolDescriptor;
import hudson.tools.ToolInstallation;
import hudson.tools.ToolProperty;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 */
public class IojsInstallation extends ToolInstallation
        implements EnvironmentSpecific<IojsInstallation>, NodeSpecific<IojsInstallation>, Serializable {

    private static final String WINDOWS_IOJS_COMMAND = "iojs.exe";
    private static final String UNIX_IOJS_COMMAND = "iojs";

    private final String iojsHome;

    @DataBoundConstructor
    public IojsInstallation(String name, String home, List<? extends ToolProperty<?>> properties) {
        super(name, launderHome(home), properties);
        this.iojsHome = super.getHome();
    }

    private static String launderHome(String home) {
        if (home.endsWith("/") || home.endsWith("\\")) {
            // see https://issues.apache.org/bugzilla/show_bug.cgi?id=26947
            // Ant doesn't like the trailing slash, especially on Windows
            return home.substring(0, home.length() - 1);
        } else {
            return home;
        }
    }

    @Override
    public String getHome() {
        if (iojsHome != null) {
            return iojsHome;
        }
        return super.getHome();
    }

    public IojsInstallation forEnvironment(EnvVars environment) {
        return new IojsInstallation(getName(), environment.expand(iojsHome), getProperties().toList());
    }

    public IojsInstallation forNode(Node node, TaskListener log) throws IOException, InterruptedException {
        return new IojsInstallation(getName(), translateFor(node, log), getProperties().toList());
    }

    public String getExecutable(Launcher launcher) throws InterruptedException, IOException {
        return launcher.getChannel().call(new Callable<String, IOException>() {
            public String call() throws IOException {
                File exe = getExeFile();
                if (exe.exists()) {
                    return exe.getPath();
                }
                return null;
            }
        });
    }

    private File getExeFile() {
        String execName = (Functions.isWindows()) ? WINDOWS_IOJS_COMMAND : UNIX_IOJS_COMMAND;
        String iojsHome = Util.replaceMacro(this.iojsHome, EnvVars.masterEnvVars);
        return new File(iojsHome, "bin/" + execName);
    }

    @Extension
    public static class DescriptorImpl extends ToolDescriptor<IojsInstallation> {

        public DescriptorImpl() {
        }

        @Override
        public String getDisplayName() {
            return jenkins.plugins.iojs.tools.Messages.installer_displayName();
        }

        // Persistence is done by IojsPlugin

        @Override
        public IojsInstallation[] getInstallations() {
            return IojsPlugin.instance().getInstallations();
        }

        @Override
        public void setInstallations(IojsInstallation... installations) {
            IojsPlugin.instance().setInstallations(installations);
        }
    }
}
