package jenkins.plugins.iojs.tools;

import jenkins.plugins.iojs.tools.pathresolvers.LatestInstallerPathResolver;
import hudson.tools.DownloadFromUrlInstaller;

/**
 */
public interface InstallerPathResolver {
    String resolvePathFor(String version, IojsInstaller.Platform platform,
    IojsInstaller.CPU cpu);
    String extractArchiveIntermediateDirectoryName(String relativeDownloadPath);

    public static class Factory {
        public static InstallerPathResolver findResolverFor(DownloadFromUrlInstaller.Installable installable){
            if(isVersionBlacklisted(installable.id)){
                throw new IllegalArgumentException("Provided version ("+installable.id+") installer structure not (yet) supported !");
            } else {
                return new LatestInstallerPathResolver();
            }
        }

        public static boolean isVersionBlacklisted(String version){
            return false;
        }
    }
}
