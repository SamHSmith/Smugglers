package loading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import sound.Sound;
import toolbox.Maths;
import models.ModelTexture;
import models.RawModel;
import exceptions.DirectoryNotSetException;
import fontMeshCreator.FontType;

public class MasterLoader {

	public static File resFolder;
	private static ArrayList<Resource> reses = new ArrayList<Resource>();
	public static ArrayList<FontType> fonts = new ArrayList<FontType>();
	public static ArrayList<ModelTexture> textures = new ArrayList<ModelTexture>();
	public static ArrayList<RawModel> models = new ArrayList<RawModel>();
	public static ArrayList<String[]> data = new ArrayList<String[]>();
	public static ArrayList<Sound> sounds = new ArrayList<Sound>();

	private static ModelLoader loader;
	public static boolean loading = false;

	public static void init(ModelLoader loader) throws DirectoryNotSetException {
		MasterLoader.loader = loader;
		if (!load()) {
			throw new DirectoryNotSetException(
					"The resFolder for all the resources in the program has not been set");
		}
	}

	private static boolean load() {
		if (resFolder == null) {
			return false;
		}
		System.out.println("Loading Files");
		loading = true;
		if (!resFolder.exists()) {
			System.err.println("There is no res folder");
			System.err.println(resFolder.getAbsolutePath() + " is not there");
			System.out.println("Creating res folder");
			resFolder.mkdir();
			System.out.println("Creation succes");
			System.out.println("Proseding with loding operation");
		}
		File[] dirs = resFolder.listFiles();

		for (int i = 0; i < dirs.length; i++) {
			File res = dirs[i];
			File[] files = res.listFiles();

			String resname = Maths.baddCharecterFilter(res.getName());

			int j = 0;
			int oldressize = reses.size();
			loading = true;
			while (loading) {

				if (j >= files.length) {
					loading = false;
					continue;
				}
				if (files.length > 0) {
					File file = files[j];

					try {
						if (resname.equals("Model")) {

							models.add(ObjFileLoader.loadObjModel(file, loader));
							reses.add(new Resource(models.size() - 1,
									ResourceType.Model, file.getName()));
						} else if (resname.equals("Fonts")) {
							File[] ffiles = file.listFiles();
							File texture = null;
							File fontfile = null;

							for (int k = 0; k < ffiles.length; k++) {
								File ffile = ffiles[k];

								System.out.println(ffile.getName());

								if (ffile.getName().equals(
										Maths.baddCharecterFilter("Font.fnt"))) {
									fontfile = ffile;
								}
								if (ffile.getName().equals(
										Maths.baddCharecterFilter("Font.png"))) {
									texture = ffile;
								}
							}

							fonts.add(new FontType(loader
									.loadFontTexture(texture), fontfile));
							reses.add(new Resource(fonts.size() - 1,
									ResourceType.Font, file.getName()));
							System.out.println("Loaded: "
									+ file.getAbsolutePath());

						} else if (resname.equals(Maths
								.baddCharecterFilter("Textures"))) {
							textures.add(new ModelTexture(loader
									.loadTexture(file)));
							reses.add(new Resource(textures.size() - 1,
									ResourceType.Texture, file.getName()));
							System.out.println("Loaded: "
									+ file.getAbsolutePath());

						} else if (resname.equals(Maths
								.baddCharecterFilter("Data"))) {
							BufferedReader br;
							String[] text;
							ArrayList<String> strings = null;
							try {
								br = new BufferedReader(new FileReader(file));

								while (true) {
									strings = new ArrayList<String>();
									strings.add(br.readLine());
								}

							} catch (FileNotFoundException e) {
								System.err.println("Could not load:"
										+ file.getName());
								System.err
										.println("Please report bug this becouse this should not happen for what ever reason");
								System.err
										.println("It should not be possible for this to happen");
								e.printStackTrace();
							} catch (IOException e) {
								System.out.println("File: " + file.getName()
										+ " has fineshed reading");
							}

							if (strings != null) {
								text = new String[strings.size()];

								data.add(text);
								reses.add(new Resource(data.size() - 1,
										ResourceType.Data, file.getName()
												.split(".")[0]));
								System.out.println("Loaded: "
										+ file.getAbsolutePath());
							}
						} else if (resname.equals(Maths
								.baddCharecterFilter("Sound"))) {
							try {
								sounds.add(new Sound(file));
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							reses.add(new Resource(sounds.size() - 1,
									ResourceType.Sound, file.getName()));
							System.out.println("Loaded: "
									+ file.getAbsolutePath());
						}
					} catch (Exception e) {
						System.err.println("There was an error while loading "
								+ file.getName());
						System.err.println(e.getMessage());
						e.printStackTrace();
					}

					if (reses.size() > oldressize) {
						System.out.println("AN ITEM WAS LOADED");
						oldressize = reses.size();
					}
				} else {
					loading = false;
				}
				j++;
			}

		}

		System.out.println("Load Complete");
		System.out.println("Loaded: ");
		for (Iterator<Resource> iterator = reses.iterator(); iterator.hasNext();) {
			Resource res = (Resource) iterator.next();
			System.out.println("Name: " + res.getName() + " Type: "
					+ res.getRestype() + " Id: " + res.getId());
		}
		return true;
	}

	/*
	 * Gets a Resource pointer returns null if there is no pointer with the
	 * given name
	 */
	public static Resource getResource(String name) {
		System.out.println("Searching for resource of the name "+name);
		for (Iterator<Resource> iterator = reses.iterator(); iterator.hasNext();) {
			Resource res = (Resource) iterator.next();
			if (res.getName().equals(Maths.baddCharecterFilter(name))) {
				System.out.println("Resource found...      "+name);
				return res;
			}

		}
		
		System.out.println("Resource not found");
		return null;
	}

	public static void cleanup() {
		for (Iterator<Sound> iterator = sounds.iterator(); iterator.hasNext();) {
			Sound s = (Sound) iterator.next();

			s.destroy();
		}

	}

}
