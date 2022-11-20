package com.test.initializr.api;

import com.test.initializr.request.CustomProjectRequest;
import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.controller.ProjectGenerationController;
import io.spring.initializr.web.project.ProjectGenerationInvoker;
import io.spring.initializr.web.project.ProjectGenerationResult;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Controller extends ProjectGenerationController<CustomProjectRequest> {

  private final ProjectGenerationInvoker<CustomProjectRequest> projectGenerationInvoker;

  private final InitializrMetadataProvider metadataProvider;

  public Controller(
          InitializrMetadataProvider metadataProvider,
          ProjectGenerationInvoker projectGenerationInvoker) {
    super(metadataProvider, projectGenerationInvoker);
    this.projectGenerationInvoker = projectGenerationInvoker;
    this.metadataProvider = metadataProvider;
  }

  @RequestMapping({"/starter-custom.zip"})
  public ResponseEntity<byte[]> springZip(@RequestBody CustomProjectRequest request) throws IOException {

    ProjectGenerationResult result =
        this.projectGenerationInvoker.invokeProjectStructureGeneration(request);
    Path archive =
        this.createArchive(
            result,
            "zip",
            ZipArchiveOutputStream::new,
            ZipArchiveEntry::new,
            ZipArchiveEntry::setUnixMode);

    return this.upload(
        archive,
        result.getRootDirectory(),
        this.generateFileName(request, "zip"),
        "application/zip");
  }

  @Override
  public CustomProjectRequest projectRequest(Map<String, String> headers) {
    CustomProjectRequest customProjectRequest = new CustomProjectRequest();
    customProjectRequest.getParameters().putAll(headers);
    customProjectRequest.initialize(getMetadata());
    return null;
  }

  private <T extends ArchiveEntry> Path createArchive(
      ProjectGenerationResult result,
      String fileExtension,
      Function<OutputStream, ? extends ArchiveOutputStream> archiveOutputStream,
      BiFunction<File, String, T> archiveEntry,
      BiConsumer<T, Integer> setMode)
      throws IOException {
    Path archive =
        this.projectGenerationInvoker.createDistributionFile(
            result.getRootDirectory(), "." + fileExtension);
    String wrapperScript = getWrapperScript(result.getProjectDescription());
    ArchiveOutputStream output =
        (ArchiveOutputStream) archiveOutputStream.apply(Files.newOutputStream(archive));
    Throwable var9 = null;

    try {
      Files.walk(result.getRootDirectory())
          .filter(
              (path) -> {
                return !result.getRootDirectory().equals(path);
              })
          .forEach(
              (path) -> {
                try {
                  String entryName = this.getEntryName(result.getRootDirectory(), path);
                  T entry = (T) archiveEntry.apply(path.toFile(), entryName);
                  setMode.accept(entry, this.getUnixMode(wrapperScript, entryName, path));
                  output.putArchiveEntry(entry);
                  if (!Files.isDirectory(path, new LinkOption[0])) {
                    Files.copy(path, output);
                  }

                  output.closeArchiveEntry();
                } catch (IOException var10) {
                  throw new IllegalStateException(var10);
                }
              });
    } catch (Throwable var18) {
      var9 = var18;
      throw var18;
    } finally {
      if (output != null) {
        if (var9 != null) {
          try {
            output.close();
          } catch (Throwable var17) {
            var9.addSuppressed(var17);
          }
        } else {
          output.close();
        }
      }
    }

    return archive;
  }

  private String getEntryName(Path root, Path path) {
    String entryName = root.relativize(path).toString().replace('\\', '/');
    if (Files.isDirectory(path, new LinkOption[0])) {
      entryName = entryName + "/";
    }

    return entryName;
  }

    private int getUnixMode(String wrapperScript, String entryName, Path path) {
        return Files.isDirectory(path, new LinkOption[0]) ? 16877 : '耀' | (entryName.equals(wrapperScript) ? 493 : 420);
    }

  private static String getWrapperScript(ProjectDescription description) {
    BuildSystem buildSystem = description.getBuildSystem();
    String script = buildSystem.id().equals("maven") ? "mvnw" : "gradlew";
    return description.getBaseDirectory() != null
        ? description.getBaseDirectory() + "/" + script
        : script;
  }

  private String generateFileName(CustomProjectRequest request, String extension) {
    String candidate =
        StringUtils.hasText(request.getArtifactId())
            ? request.getArtifactId()
            : this.metadataProvider.get().getArtifactId().getContent();
    String tmp = candidate.replaceAll(" ", "_");

    try {
      return URLEncoder.encode(tmp, "UTF-8") + "." + extension;
    } catch (UnsupportedEncodingException var6) {
      throw new IllegalStateException("Cannot encode URL", var6);
    }
  }

  private ResponseEntity<byte[]> upload(Path archive, Path dir, String fileName, String contentType)
      throws IOException {
    byte[] bytes = Files.readAllBytes(archive);
    ResponseEntity<byte[]> result = this.createResponseEntity(bytes, contentType, fileName);
    this.projectGenerationInvoker.cleanTempFiles(dir);
    return result;
  }

  private ResponseEntity<byte[]> createResponseEntity(
      byte[] content, String contentType, String fileName) {
    String contentDispositionValue = "attachment; filename=\"" + fileName + "\"";
    return ((ResponseEntity.BodyBuilder)
            ((ResponseEntity.BodyBuilder)
                    ResponseEntity.ok().header("Content-Type", new String[] {contentType}))
                .header("Content-Disposition", new String[] {contentDispositionValue}))
        .body(content);
  }
}
