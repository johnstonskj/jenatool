package org.johnstonshome.jenatool.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

public class FileUtils {

	public static IFolder createFolder(IProject project, String folderName, IProgressMonitor monitor) throws CoreException {
		IFolder folder = project.getFolder(folderName);
		folder.create(true, true, monitor);
		return folder;
	}
	
	public static IFile getExistingFileByPath(String path) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(path));
		if (!resource.exists() || !(resource instanceof IFile)) {
			throwCoreException("File \"" + path + "\" does not exist.");
		}
		return (IFile) resource;
	}
	
	public static IContainer getExistingContainerByPath(String path) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(path));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + path + "\" does not exist.");
		}
		return (IContainer) resource;
	}
	
	public static IFile createFile(IContainer container, String fileName, String content, IProgressMonitor monitor) throws CoreException {
		final IFile file = container.getFile(new Path(fileName));
		ByteArrayInputStream in = null;
		try {
			in = new ByteArrayInputStream(content.getBytes());
			if (file.exists()) {
				file.setContents(in, true, true, monitor);
			} else {
				file.create(in, true, monitor);
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	private static void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "org.johnstonshome.jenatool.ui", IStatus.OK, message, null);
		throw new CoreException(status);
	}
}
