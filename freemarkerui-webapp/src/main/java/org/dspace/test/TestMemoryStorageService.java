package org.dspace.test;

import org.dspace.services.MetadataFieldService;
import org.dspace.services.MetadataSchemaService;
import org.dspace.services.exceptions.StorageWriteException;
import org.dspace.services.model.*;
import org.dspace.services.storage.MemoryStorageService;
import org.dspace.utils.DSpace;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Temporary class for initializing the repository with test data
 */
public class TestMemoryStorageService extends MemoryStorageService {

    private boolean initialized;

    public TestMemoryStorageService() {
        super();
        initialized = false;
    }

    private void init(){
        //Make sure we only initialize once
        if(!initialized){
            initialized = true;
            try {

                //TODO: a StorageProperty should only contain storable beans
                DSpace dspace = new DSpace();
                MetadataSchemaService metadataSchemaService = dspace.getSingletonService(MetadataSchemaService.class);
                MetadataFieldService metadataFieldService = dspace.getSingletonService(MetadataFieldService.class);


                //TODO: put this in TestMemoryMetadaschema.java or something of the like ?
                MetadataSchema dc = new MetadataSchema("http://purl.org/dc/elements/1.1/","dc");
                metadataSchemaService.create(dc);
                MetadataSchema terms = new MetadataSchema("http://purl.org/dc/terms/","terms");
                metadataSchemaService.create(terms);
                MetadataSchema foaf = new MetadataSchema("http://xmlns.com/foaf/0.1","foaf");
                metadataSchemaService.create(foaf);
                MetadataSchema premis = new MetadataSchema("http://www.loc.gov/standards/premis","premis");
                metadataSchemaService.create(premis);
                MetadataSchema bibo = new MetadataSchema("http://purl.org/ontology/bibo/","bibo");
                metadataSchemaService.create(bibo);


                // Time to create our dublin core fields in which we store our values
                MetadataField title = new MetadataField(dc.getPrefix(), "title", "Typically, a Title will be a name by which the resource is formally known.");
                metadataFieldService.create(title);
                MetadataField subject = new MetadataField(dc.getPrefix(), "subject", "Typically, the subject will be represented using keywords, key phrases, or classification codes. Recommended best practice is to use a controlled vocabulary. To describe the spatial or temporal topic of the resource, use the Coverage element.");
                metadataFieldService.create(subject);
                MetadataField type = new MetadataField(dc.getPrefix(), "type", "The nature or genre of the resource.");
                metadataFieldService.create(type);

                MetadataField identifier = new MetadataField(dc.getPrefix(), "identifier", "An unambiguous reference to the resource within a given context.");
                metadataFieldService.create(identifier);
                MetadataField description = new MetadataField(dc.getPrefix(), "description", "Description may include but is not limited to: an abstract, a table of contents, a graphical representation, or a free-text account of the resource.");
                metadataFieldService.create(description);
                MetadataField source = new MetadataField(dc.getPrefix(), "source", "A related resource from which the described resource is derived.");
                metadataFieldService.create(source);

                MetadataField inputCreator = new MetadataField(terms.getPrefix(), "creator", "Examples of a Creator include a person, an organization, or a service. Typically, the name of a Creator should be used to indicate the entity.");
                metadataFieldService.create(inputCreator);
                MetadataField dataIssued = new MetadataField(terms.getPrefix(), "issued", "Date of formal issuance (e.g., publication) of the resource.");
                metadataFieldService.create(dataIssued);
                MetadataField abstractField = new MetadataField(terms.getPrefix(), "abstract", " A summary of the resource.");
                metadataFieldService.create(abstractField);
                MetadataField termsIsPartOf = new MetadataField(terms.getPrefix(), "isPartOf", "A related resource in which the described resource is physically or logically included.");
                metadataFieldService.create(termsIsPartOf);

                MetadataField chapter = new MetadataField(bibo.getPrefix(), "chapter", "");
                metadataFieldService.create(chapter);
                MetadataField volume = new MetadataField(bibo.getPrefix(), "volume", "");
                metadataFieldService.create(volume);
                MetadataField issue = new MetadataField(bibo.getPrefix(), "issue", "");
                metadataFieldService.create(issue);
                MetadataField startPage = new MetadataField(bibo.getPrefix(), "pageStart", "");
                metadataFieldService.create(startPage);
                MetadataField endPage = new MetadataField(bibo.getPrefix(), "pageEnd", "");
                metadataFieldService.create(endPage);
                MetadataField isbn = new MetadataField(bibo.getPrefix(), "isbn", "");
                metadataFieldService.create(isbn);

                MetadataField givenname = new MetadataField(foaf.getPrefix(), "givenname", "The given name of some person");
                metadataFieldService.create(givenname);
                MetadataField family_name = new MetadataField(foaf.getPrefix(), "family_name", "The family_name of some person");
                metadataFieldService.create(family_name);
                MetadataField openid = new MetadataField(foaf.getPrefix(), "openid", "An OpenID for an Agent");
                metadataFieldService.create(openid);
                MetadataField img = new MetadataField(foaf.getPrefix(), "img", "An image that can be used to represent some thing (ie. those depictions which are particularly representative of something, eg. one's photo on a homepage)");
                metadataFieldService.create(img);
                MetadataField foafTitle = new MetadataField(foaf.getPrefix(), "title", "Title (Mr, Mrs, Ms, Dr. etc)");
                metadataFieldService.create(foafTitle);
                MetadataField interest = new MetadataField(foaf.getPrefix(), "interest", "A page about a topic of interest to this person.");
                metadataFieldService.create(interest);
                MetadataField phone = new MetadataField(foaf.getPrefix(), "phone", "A phone, specified using fully qualified tel: URI scheme (refs: http://www.w3.org/Addressing/schemes.html#tel).");
                metadataFieldService.create(phone);


                MetadataField originalName = new MetadataField(premis.getPrefix(), "originalName", "The original name of the file");
                metadataFieldService.create(originalName);
                MetadataField size = new MetadataField(premis.getPrefix(), "size", "The filesize in bytes");
                metadataFieldService.create(size);
                MetadataField messageDigest = new MetadataField(premis.getPrefix(), "messageDigest", "The MD5 HASH");
                metadataFieldService.create(messageDigest);

                //TODO: create publisher entity: Publisher: 	New York : American Museum of Natural History


                //Create our storage entities
                StorageEntity museum = new StorageEntity("museum", "/", new StorageProperty[] {
                        new StorageProperty(title, "American Museum of Natural History"),
                        new StorageProperty(type, "museum"),
                });


                createEntity(museum);
                StorageEntity library = new StorageEntity("library", "/museum", new StorageProperty[] {
                        new StorageProperty(title, "Library"),
                        new StorageProperty(type, "library"),
                });
                createEntity(library);

                StorageEntity people = new StorageEntity("people", "/", new StorageProperty[] {
                        new StorageProperty(title, "Browse authors"),
                });
                createEntity(people);

                //author pt 1
                StorageEntity jonaitis = new StorageEntity("jonaitis", "/people", new StorageProperty[] {
                        new StorageProperty(title, "jonaitis"),
                        new StorageProperty(type, "person"),
                        new StorageProperty(givenname, "Aldona"),
                        new StorageProperty(family_name, "Jonaitis"),
                        new StorageProperty(openid, "123456789"),
                });
                createEntity(jonaitis);

                //author pt 2
                StorageEntity freedR = new StorageEntity("freedR", "/people", new StorageProperty[] {
                        new StorageProperty(title, "freedR"),
                        new StorageProperty(type, "person"),
                        new StorageProperty(givenname, "Ruth S."),
                        new StorageProperty(family_name, "Freed"),
                        new StorageProperty(openid, "123456789"),
                });
                createEntity(freedR);

                //author pt 2
                StorageEntity freedS = new StorageEntity("freedS", "/people", new StorageProperty[] {
                        new StorageProperty(title, "freedS"),
                        new StorageProperty(type, "person"),
                        new StorageProperty(givenname, "Stanley A."),
                        new StorageProperty(family_name, "Freed"),
                        new StorageProperty(openid, "123456789"),
                });
                createEntity(freedS);

                //author pt 3
                StorageEntity spencer = new StorageEntity("spencer", "/people", new StorageProperty[] {
                        new StorageProperty(title, "spencer"),
                        new StorageProperty(type, "person"),
                        new StorageProperty(givenname, "Larsen"),
                        new StorageProperty(family_name, "Clark Spencer"),
                        new StorageProperty(openid, "123456789"),
                        new StorageProperty(foafTitle, "Mr."),
                        new StorageProperty(interest, "The intersection of libraries, technology and the educational missions of higher education institutions"),
                        new StorageProperty(phone, "+15312648751"),
                });
                createEntity(spencer);


                //author pt 3
                StorageEntity thomas = new StorageEntity("thomas", "/people", new StorageProperty[] {
                        new StorageProperty(title, "thomas"),
                        new StorageProperty(type, "person"),
                        new StorageProperty(givenname, "David Hurst"),
                        new StorageProperty(family_name, "Thomas"),
                        new StorageProperty(openid, "123456789"),
                });
                createEntity(thomas);






                StorageEntity files = new StorageEntity("files", "/", new StorageProperty[]{
                    new StorageProperty(title, "Files")
                });
                createEntity(files);

                StorageEntity spencerDescr = new StorageEntity("filedescriptor", "/files", new StorageProperty[]{
                    new StorageProperty(title, "Picture"),
                });
                try {
                    URL resource = new URL("http://atmire.com/dspace2-demo/larsen1.jpg");
                    URLConnection urlConnection = resource.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    spencerDescr.addProperty(new StorageProperty("inputstream", inputStream));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                createEntity(spencerDescr);

                spencer.addProperty(new StorageRelation("hasPicture", spencerDescr));
                spencerDescr.addProperty(new StorageRelation("ispictureof", spencer));







                StorageEntity series1 = new StorageEntity("series1", "/", new StorageProperty[]{
                    new StorageProperty(title, "Anthropological Papers of the American Museum of Natural History"),
                    new StorageProperty(description, "The Anthropological Papers, published continuously since 1907, are monographic volumes that include some of the great ethnographies of the 20th century, particularly on North American Indians. Several illustrious anthropologists published their work in the Anthropological Papers, as well as many past and present curators of the AMNH Division of Anthropology. Prior to 1930, large special reports were published in the Memoirs."),
                });
                createEntity(series1);

                StorageEntity series2 = new StorageEntity("series2", "/", new StorageProperty[]{
                    new StorageProperty(title, "American Museum Novitates"),
                });
                createEntity(series2);

                StorageEntity series3 = new StorageEntity("series3", "/", new StorageProperty[]{
                    new StorageProperty(title, "Bulletin of the American Museum of Natural History"),
                });
                createEntity(series3);

                StorageEntity series4 = new StorageEntity("series4", "/", new StorageProperty[]{
                    new StorageProperty(title, "Manuscripts in the AMNH Library"),
                });
                createEntity(series4);

                StorageEntity series5 = new StorageEntity("series5", "/", new StorageProperty[]{
                    new StorageProperty(title, "Memoirs of the American Museum of Natural History"),
                });
                createEntity(series5);








                StorageEntity book1 = new StorageEntity("volume57", "/series1", new StorageProperty[]{
                    new StorageProperty(title, "The anthropology of St. Catherines Island."),
                    new StorageProperty(dataIssued, "1982"),
                    new StorageProperty(type, "volume"),
                    new StorageProperty(subject, "Paleopathology"),
                    new StorageProperty(subject, "Prehistoric"),
                    new StorageProperty(subject, "Archaeology"),
                    new StorageProperty(subject, "Indians of North America"),
                    new StorageProperty(volume, "57"),
                    new StorageProperty(isbn, "026256212X"),
                });
                createEntity(book1);

                library.addProperty(new StorageRelation("hasBook", book1));
                book1.addProperty(new StorageRelation("ispartof", library));


                StorageEntity book1chapter1 = new StorageEntity("part1", "/series1/volume57", new StorageProperty[]{
                    new StorageProperty(title, "Tlingit halibut hooks : an analysis of the visual symbols of a rite of passage."),
                    new StorageProperty(chapter, "1"),
                    new StorageProperty(type, "part")
                });
                createEntity(book1chapter1);

                book1chapter1.addProperty(new StorageRelation("hasAuthor", jonaitis));
                jonaitis.addProperty(new StorageRelation("isauthorof", book1chapter1));
                book1chapter1.addProperty(new StorageRelation("isChapterOf", book1));
                book1.addProperty(new StorageRelation("hasChapter", book1chapter1));


                StorageEntity book1chapter2 = new StorageEntity("part2", "/series1/volume57", new StorageProperty[]{
                    new StorageProperty(title, "Enculturation and education in Shanti Nagar."),
                    new StorageProperty(chapter, "2"),
                    new StorageProperty(type, "part")
                });
                createEntity(book1chapter2);

                book1chapter2.addProperty(new StorageRelation("hasAuthor", freedR));
                book1chapter2.addProperty(new StorageRelation("hasAuthor", freedS));
                freedR.addProperty(new StorageRelation("isauthorof", book1chapter2));
                freedS.addProperty(new StorageRelation("isauthorof", book1chapter2));
                book1chapter2.addProperty(new StorageRelation("isChapterOf", book1));
                book1.addProperty(new StorageRelation("hasChapter", book1chapter2));

                StorageEntity file1 = new StorageEntity("file1", "/files", new StorageProperty[]{
                    new StorageProperty(title, "Scanned Text"),
                    new StorageProperty(originalName, "A057a03.pdf"),
                    new StorageProperty(size, "19898739"),
                    new StorageProperty(messageDigest, "421c47cd18228dded79d3cd882cd26cc"),
                    new StorageProperty(type, "file")
                });
//                try {
//                    URL resource = new URL("http://atmire.com/dspace2-demo/A057a03.pdf");
//                    URLConnection urlConnection = resource.openConnection();
//                    InputStream inputStream = urlConnection.getInputStream();
//                    file1.addProperty(new StorageProperty("inputstream", inputStream));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                createEntity(file1);

                StorageEntity file1thumb = new StorageEntity("file1thumb", "/files", new StorageProperty[]{
                    new StorageProperty(title, "Thumbnail"),
                    new StorageProperty(originalName, "A057a03.pdf.png"),
                    new StorageProperty(size, "12067"),
                    new StorageProperty(messageDigest, "74656cccd9a7592d4c92ed1910544c9d"),
                    new StorageProperty(type, "file")
                });
                try {
                    URL resource = new URL("http://atmire.com/dspace2-demo/A057a03.pdf.png");
                    URLConnection urlConnection = resource.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    file1thumb.addProperty(new StorageProperty("inputstream", inputStream));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                createEntity(file1thumb);

                StorageEntity file1plaintext = new StorageEntity("file1plaintext", "/files", new StorageProperty[]{
                    new StorageProperty(title, "Plain Text"),
                    new StorageProperty(originalName, "A057a03.pdf.txt"),
                    new StorageProperty(size, "346886"),
                    new StorageProperty(messageDigest, "a7fe819fb0a322c5152ac754945dfa56"),
                    new StorageProperty(type, "file")
                });
                try {
                    URL resource = new URL("http://atmire.com/dspace2-demo/A057a03.pdf.txt");
                    URLConnection urlConnection = resource.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    file1plaintext.addProperty(new StorageProperty("inputstream", inputStream));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                createEntity(file1plaintext);

                file1.addProperty(new StorageRelation("hasThumbnail", file1thumb));
                file1thumb.addProperty(new StorageRelation("isThumbnailOf", file1));
                file1.addProperty(new StorageRelation("hasPlaintext", file1plaintext));
                file1plaintext.addProperty(new StorageRelation("isPlaintextOf", file1));

                StorageEntity book1chapter3 = new StorageEntity("part3", "/series1/volume57", new StorageProperty[]{
                    new StorageProperty(title, "The anthropology of St. Catherines Island. 3, Prehistoric human biological adaptation."),
                    new StorageProperty(chapter, "3"),
                    new StorageProperty(type, "part"),
                    new StorageProperty(abstractField, "Skeletal remains from the prehistoric coast of Georgia are the basis for this study. The effects of agriculture on the human skeleton are examined and explained in the present paper. The region was chosen because (1) there is a large skeletal series representative of both an early preagricultural adapatation (2200 B.C.-A.D. 1150) and a later mixed agricultural and hunting-gathering adaptation (A.D. 1150-A.D. 1550); (2) the Georgia coast represents continuous in situ cultural development from at least 2200 B.C. to A.D. 1550, implying human biological continuity for at least 3500 years prior to European contact; and (3) the economic regime for the Georgia coast has been documented by a large body of archaeological and ethnohistoric data. A series of skeletal and dental changes are viewed in light of an adaptational model encompassing disease and size of the hard tissues - skeletal and dental - and their respective responses to the behavioral shift from a hunting and gathering lifeway to..."),
                    new StorageProperty(startPage, "157"),
                    new StorageProperty(endPage, "270"),
                });
                createEntity(book1chapter3);

                book1chapter3.addProperty(new StorageRelation("hasAuthor", spencer));
                book1chapter3.addProperty(new StorageRelation("hasAuthor", thomas));
                spencer.addProperty(new StorageRelation("isauthorof", book1chapter3));
                thomas.addProperty(new StorageRelation("isauthorof", book1chapter3));
                book1chapter3.addProperty(new StorageRelation("isChapterOf", book1));
                book1.addProperty(new StorageRelation("hasChapter", book1chapter3));

                book1chapter3.addProperty(new StorageRelation("hasTextVersion", file1));
                file1.addProperty(new StorageRelation("isTextVersionOf", book1chapter3));

                StorageEntity book2chapter1 = new StorageEntity("book2part1", "/series2/volume1", new StorageProperty[]{
                    new StorageProperty(title, "Bioarchaeology: Interpreting Behavior from the Human Skeleton"),
                    new StorageProperty(chapter, "1"),
                    new StorageProperty(type, "part"),
                });
                createEntity(book2chapter1);

                book2chapter1.addProperty(new StorageRelation("hasAuthor", spencer));
                spencer.addProperty(new StorageRelation("isauthorof", book2chapter1));


                StorageEntity book1chapter4 = new StorageEntity("part4", "/series1/volume57", new StorageProperty[]{
                    new StorageProperty(title, "The anthropology of St. Catherines Island. 4, The St. Catherines period mortuary complex."),
                    new StorageProperty(chapter, "4"),
                    new StorageProperty(type, "part")
                });
                createEntity(book1chapter4);
                book1chapter4.addProperty(new StorageRelation("isChapterOf", book1));
                book1.addProperty(new StorageRelation("hasChapter", book1chapter4));

                StorageEntity book1chapter5 = new StorageEntity("part5", "/series1/volume57", new StorageProperty[]{
                    new StorageProperty(title, "A review of the European primate genus Anchomomys and some allied forms."),
                    new StorageProperty(chapter, "5"),
                    new StorageProperty(type, "part")
                });
                createEntity(book1chapter5);
                book1chapter5.addProperty(new StorageRelation("isChapterOf", book1));
                book1.addProperty(new StorageRelation("hasChapter", book1chapter5));

                book1chapter2.addProperty(new StorageRelation("previousSibling", book1chapter1));
                book1chapter3.addProperty(new StorageRelation("previousSibling", book1chapter2));
                book1chapter4.addProperty(new StorageRelation("previousSibling", book1chapter3));
                book1chapter5.addProperty(new StorageRelation("previousSibling", book1chapter4));
                book1chapter1.addProperty(new StorageRelation("nextSibling", book1chapter2));
                book1chapter2.addProperty(new StorageRelation("nextSibling", book1chapter3));
                book1chapter3.addProperty(new StorageRelation("nextSibling", book1chapter4));
                book1chapter4.addProperty(new StorageRelation("nextSibling", book1chapter5));


            } catch (StorageWriteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public StorageEntity getEntity(String entityId) {
        init();
        return super.getEntity(entityId);
    }

    @Override
    public List<StorageEntity> getEntities(String path) {
        init();
        return super.getEntities(path);
    }

    @Override
    public String createEntity(StorageEntity storageEntity) {
        init();
        super.createEntity(storageEntity);
        return storageEntity.getId();
    }

    @Override
    public boolean deleteEntity(String entityId) {
        init();
        return super.deleteEntity(entityId);
    }

    @Override
    public boolean exists(String entityId) {
        init();
        return super.exists(entityId);
    }
}
