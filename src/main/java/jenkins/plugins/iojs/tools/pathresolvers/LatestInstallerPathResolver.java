package jenkins.plugins.iojs.tools.pathresolvers;

import jenkins.plugins.iojs.tools.InstallerPathResolver;
import jenkins.plugins.iojs.tools.IojsInstaller;

import java.util.logging.Logger;

/**
 * @author fcamblor
 */
public class LatestInstallerPathResolver implements InstallerPathResolver {

    private static final Logger LOGGER = Logger.getLogger(LatestInstallerPathResolver.class.getName());

    private static final String EXTENSION = ".tar.gz";

    public String resolvePathFor(String version, IojsInstaller.Platform platform, IojsInstaller.CPU cpu) {
        String r = null;
        if(platform== IojsInstaller.Platform.MAC){
            if(cpu == IojsInstaller.CPU.amd64){
                r = "iojs-v"+version+"-darwin-x64"+EXTENSION;
            } else if(cpu == IojsInstaller.CPU.i386){
                r = "iojs-v"+version+"-darwin-x86"+EXTENSION;
            }
        } else if(platform == IojsInstaller.Platform.LINUX){
            if(cpu == IojsInstaller.CPU.amd64){
                r = "iojs-v"+version+"-linux-x64"+EXTENSION;
            } else if(cpu == IojsInstaller.CPU.i386){
                r = "iojs-v"+version+"-linux-x86"+EXTENSION;
            }
        // At the moment, windows MSI installer are not handled !
        //} else if (platform == IojsInstaller.Platform.WINDOWS){
        }
        if (r == null) {
            throw new IllegalArgumentException("Unresolvable io.js installer for version=" + version +
                    ", platform=" + platform.name() +
                    ", cpu=" + cpu.name());
        }
        r = r + EXTENSION;
        LOGGER.fine("Resolved path " + r + " for platform " + platform + " and cpu = " + cpu);
        return r;
    }

    public String extractArchiveIntermediateDirectoryName(String relativeDownloadPath) {
        return relativeDownloadPath.substring(relativeDownloadPath.lastIndexOf("/")+1, relativeDownloadPath.lastIndexOf(EXTENSION));
    }
}
