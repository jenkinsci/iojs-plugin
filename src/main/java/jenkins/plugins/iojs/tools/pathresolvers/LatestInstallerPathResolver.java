package jenkins.plugins.nodejs.tools.pathresolvers;

import jenkins.plugins.nodejs.tools.InstallerPathResolver;
import jenkins.plugins.nodejs.tools.NodeJSInstaller;

/**
 * @author fcamblor
 */
public class LatestInstallerPathResolver implements InstallerPathResolver {
    private static final String EXTENSION = ".tar.gz";

    public String resolvePathFor(String version, IojsInstaller.Platform platform, NodeJSInstaller.CPU cpu) {
        if(platform== IojsInstaller.Platform.MAC){
            if(cpu == IojsInstaller.CPU.amd64){
                return "iojs-v"+version+"-darwin-x64"+EXTENSION;
            } else if(cpu == IojsInstaller.CPU.i386){
                return "iojs-v"+version+"-darwin-x86"+EXTENSION;
            }
        } else if(platform == IojsInstaller.Platform.LINUX){
            if(cpu == IojsInstaller.CPU.amd64){
                return "iojs-v"+version+"-linux-x64"+EXTENSION;
            } else if(cpu == IojsInstaller.CPU.i386){
                return "iojs-v"+version+"-linux-x86"+EXTENSION;
            }
        // At the moment, windows MSI installer are not handled !
        //} else if (platform == NodeJSInstaller.Platform.WINDOWS){
        }
        throw new IllegalArgumentException("Unresolvable io.js installer for version="+version+", platform="+platform.name()+", cpu="+cpu.name());
    }

    public String extractArchiveIntermediateDirectoryName(String relativeDownloadPath) {
        return relativeDownloadPath.substring(relativeDownloadPath.lastIndexOf("/")+1, relativeDownloadPath.lastIndexOf(EXTENSION));
    }
}
