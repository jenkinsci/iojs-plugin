package jenkins.plugins.iojs.tools;

import hudson.tools.DownloadFromUrlInstaller;
import jenkins.plugins.iojs.tools.pathresolvers.LatestInstallerPathResolver;

/**
 */
public interface InstallerPathResolver {
    String resolvePathFor(String version, IojsInstaller.Platform platform, IojsInstaller.CPU cpu);
    String extractArchiveIntermediateDirectoryName(String relativeDownloadPath);

    public static class Factory {
        public static InstallerPathResolver findResolverFor(DownloadFromUrlInstaller.Installable installable){
            return new LatestInstallerPathResolver();
        }
    }
}
