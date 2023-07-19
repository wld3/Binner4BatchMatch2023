package edu.umich.wld;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;


public class PostProccessConstants {
	
	public static Boolean BATCHMATCH_FORWARD_BATCHING = false;
	public static int BATCHMATCH_MAX_BATCH = 15;
	public static int BATCHMATCH_MIN_BATCH = 0;
	
	
	public static String POSTPROCESS_VERSION = "0.0.7";
//	public static String BATCHMATCH_VERSION = "0.0.3";
	public static String POSTPROCESS_TAG = "Batch";
		
	public static Integer DEFAULT_N_SEARCH_RESULTS = 100;
	public static Double DEFAULT_RT_SEARCH_TOL = 0.1;
	public static Double DEFAULT_ANNEALING_STRETCH_FACTOR = 1.7;
	public static Double DEFAULT_MASS_SEARCH_TOL = 0.1;
	
	public static Double DEFAULT_LOGISTIC_MID = 0.008;
	public static Double DEFAULT_LOGISTIC_MAX = 1.2;
	public static Double DEFAULT_LOGISTIC_CURVATURE = 1000.0;

	public static Double DEFAULT_MAX_MASS_DIFF = 10.0;
	public static Double DENSE_MASS_DIFF_THRESHOLD = 300.0;
	public static Double DEFAULT_MIN_MASS_DIFF = 0.0;
	public static Double LARGEST_MASS_DIFF = 950.0;
	
	public static String SAMPLE_ID_FORMAT ="(S)\\d{8}";
	public static String MASTER_POOL_ID_FORMAT ="CS00000MP";
	//public static String MASTER_POOL_ID_FORMAT_SHORT ="(CS)\\d{3}BPM1-\\d{1}";
	
	//CS000BPM1
	
	public static Double EPSILON = 1e-6;
	public static double BIG_NEGATIVE = -1e6;
	
	public static String [] MISSINGNESS_LIST = {"Inf", "na", ".", "NA", "N/A", "n/a", "NaN", "null", "Null", "NULL", null};

	public static String [] TIER_CHOICES = {"tier", "tiers"};
	public static String [] TIERS_ALLOWED = {"0", "1", "2"};
	
	
	public static Integer N_ANNOTATION_FILE_COLS = 5;
	public static String SINGLETON_ANNOTATION = "[M]";

	public static String [] OUTPUT_FIXED_COLUMN_LABELS = {"Index", "Feature", "Mass", "RT", "Median Intensity", 
			"KMD", "Isotopes", "Other Isotopes In Group", "Annotations", "Other Annotations in Group", "Further Annotation", "Derivations", 
			"Derived Molecular Mass" , "Mass Error", 
			"Feature Group Number", "Charge Carrier",  
			"Adduct/NL", "Bin", "Corr Cluster", "Rebin Subcluster", "RT Subcluster"}; 
	
	
	public static List<String> SKIP_FOR_BATCH_COLUMN_LABELS = Arrays.asList( new String [] {"Index", "Median Intensity", 
			"KMD", "Isotopes", "Other Isotopes In Group", "Annotations", "Other Annotations in Group", "Further Annotation", "Derivations", 
			"Derived Molecular Mass" , "Mass Error", 
			"Feature Group Number", "Charge Carrier",  
			"Adduct/NL", "Bin", "Corr Cluster", "Rebin Subcluster", "RT Subcluster"}); 
	
		
	public static List<String> SKIP_FOR_SIMPLE_COLUMN_LABELS = Arrays.asList(new String [] { "Index", "Median Intensity", "KMD",
			"Isotopes", "Other Isotopes In Group", "Annotations", "Other Annotations in Group", "Further Annotation", 
			"Mass Error", "Feature Group Number", "Bin", "Corr Cluster", "Rebin Subcluster", "RT Subcluster"}); 

	
	public static String [] POSTPROCESS_SEARCH_OUTPUT_COLUMN_LABELS = {"Match Replicate", "Compound Name", "Formula", "Compound Mass", 
			"Compound RT", "Delta RT", "Delta Mass"}; 
	
	public static List<String> POSTPROCESS_SEARCH_OUTPUT_COLUMN_TAGS = Arrays.asList(new String [] {"matchreplicate", "compoundname", "formula", 
			"compoundmass", "compoundrt", "deltart", "deltamass"}); 
	
	
	public static final Integer[] OUTPUT_FIXED_COLUMN_WIDTHS = { 5 * 256,   
			30 * 256,  
			18 * 256, //Mass
			15 * 256, //RT
			15 * 256, //MIntense
			15 * 256, //KMD
			40 * 256, //Iso
			50 * 256,  //Other Isotopes
			40 * 256, // Annotations
			50 * 256, // Other Annotations
			40 * 256, // Further Annotaions
			40 * 256, // Derivation
			18 * 256, // Derived Mal Mass
			15 * 256, // Mass Error
			8 * 256, //Feature Group No
			8 * 256, //Charge Carrier
			8 * 256, //Adduct
			8 * 256, //Bin
			8 * 256, //Cluster
			8 * 256, //Rebin Cluster
			8 * 256}; //RT Cluster
			
	public static Integer [] POSTPROCESS_SEARCH_OUTPUT_COLUMN_WIDTHS = { 8 * 256, // Match Rep
			40 * 256, // COMPOUND
			25 * 256, // FORMULA
			15 * 256, // MASS
			15 * 256, // RT
			15 * 256,  // MASS DIFF
			15 * 256};  // RT DIFF
			
	
	public static List<String> BATCHMATCH_POSSIBLE_FILE_RTTYPE_TAGS = Arrays.asList(new String [] { "obs", "exp", "par", "unk"} );
	
	public static List<String> METABOLOMICS_RECOGNIZED_COL_TAGS = Arrays.asList( new String [] { 
			"matchgroupuniquect", "matchgroupct", "batch", "batchidx", "batchno", "batchindex", 
			"rsd", "%missing", "pctmissing", "redundancygrp", "redundancygroup","rgroup",  "matchgroup", "matchgrp", "match",
			"index", "indices", "idx", 
			 "feature", "metabolite", "featurename", "metabolitename", "metabolites", "features", "compounds", "compound", "name", 
			"mass", "mz", "m/z", "m\\z", "masses",
			"rt", "retentiontime", "ri", "retentionindex",  "rts", "ris",
			"oldrt", "old_rt",  "oldretentiontime", "old_retention_time", 
			"intensity", "intensities", "medintensity", "medianintensity", "medintensities", "medianintensities", 
			"kmd", "rmd", 
			"isotopes", "isotope", "otherisotopesingroup", "otherisotopesforgroup", "otherisotope", "otherisotopes", "othergroupisotopes", "othergrpisotopes", 
			"annotation", "annotations", 
			"furtherannotation", "furtherannotations", 
			"otherannotation", "otherannotationsingroup",  "groupannotations", "groupannotation", "grpannotation", "grpannotations", 
			"derivation", "derivations", 
			"putativemolecularmass", "derivedmass",  "neutralmass", "derivedmolecularmass", "derivedmolecularmasses", "putativemass",
			"masserror", "error", 
			"molecularion", "molecularionnumber", "featuregroupnumber", "featuregroup", "featuregroupno", 
			"chargecarrier", "carrier", "cc", "chargecarriers",
			"adducts/nls", "adduct/nl", "adduct/nls", "adduct", "adductnl", "adducts", "adductornl", 
			"bin", "binindex", "binidx", 
			"corrcluster", "oldcluster", "cluster", "correlationcluster", 
			"newcluster", "rebincluster","rebinsubcluster",
			"rtcluster", "rtsubcluster","retentiontimecluster", "newnewcluster"});

	//riginal RT
	public static List<String> BATCH_MATCHGRP_CT_CHOICES_ARRAY = Arrays.asList(new String [] {"matchgroupct"});
//BatchMatchConstants
	public static List<String> BATCH_MATCHGRP_UNIQUECT_CHOICES_ARRAY = Arrays.asList(new String [] {"matchgroupuniquect"});

	public static List<String> BATCH_IDX_CHOICES_ARRAY = Arrays.asList(new String [] {"batch", "batchidx", "batchno", "batchindex"});

	public static List<String> RSD_CHOICES_ARRAY = Arrays.asList(new String []{ "rsd"});
	
	public static List<String> PCT_MISSING_CHOICES_ARRAY = Arrays.asList(new String []{ "%missing", "pctmissing"});
	
	public static List<String> REDUNDANCY_GRP_CHOICES_ARRAY = Arrays.asList(new String []{ "redundancygrp", "redundancygroup","rgroup", "matchgroup", "matchgrp", "match"});
	
	public static List<String> INDEX_CHOICES_ARRAY = Arrays.asList(new String []{ "index", "idx","indices"});
	
	public static String [] COMPOUND_CHOICES = {"compound", "feature", "featurename", 
			"metabolite", "metabolites", "metabolitename","features", "compounds", "name"};
	public static List<String> COMPOUND_CHOICES_ARRAY = Arrays.asList(COMPOUND_CHOICES);
	
	public static String [] COMPOUND_CHOICES2 = {"binnername"};//"compound", "feature", "featurename", 
	//	"metabolite", "metabolites", "metabolitename","features", "compounds", "name"};
	
	public static List<String> COMPOUND_CHOICES_ARRAY2 = Arrays.asList(COMPOUND_CHOICES2);

	public static String [] LIMITED_COMPOUND_CHOICES = {"featurename"};//"compound", "feature", "featurename", 
	public static List<String> LIMITED_COMPOUND_CHOICES_ARRAY = Arrays.asList(LIMITED_COMPOUND_CHOICES);

	public static String [] LIMITED_MASS_MEASUREMENT_CHOICES = {"monoisotopicm/z", "monoisotopicm\\z", "monoisotopicmass", "monoisotopicmz"};
	public static List<String> LIMITED_MASS_MEASUREMENT_CHOICES_ARRAY = Arrays.asList(LIMITED_MASS_MEASUREMENT_CHOICES);
	
	public static String [] MASS_CHOICES = {"mass", "mz", "m/z", "m\\z", "masses", "monoisotopicm/z"};
	public static List<String> MASS_CHOICES_ARRAY = Arrays.asList(MASS_CHOICES);
	
	public static String [] RETENTION_TIME_CHOICES = { "rt", "retentiontime", "retention index", "ri", "retentionindex", "ris", "rts"};
	public static List<String> RT_CHOICES_ARRAY = Arrays.asList(RETENTION_TIME_CHOICES);
	
	public static String [] SPECIAL_EXPECTED_RETENTION_TIME_CHOICES = {"rtexpected"};
	public static List<String> SPECIAL_EXPECTED_RETENTION_TIME_CHOICES_ARRAY = Arrays.asList(SPECIAL_EXPECTED_RETENTION_TIME_CHOICES);

	public static String [] OLD_RETENTION_TIME_CHOICES = { "oldrt", "old_rt",  "oldretentiontime", "old_retention_time"};
	public static List<String> OLD_RT_CHOICES_ARRAY = Arrays.asList(OLD_RETENTION_TIME_CHOICES);

	public static String [] FORMULA_NAME_CHOICES = {"formula", "Formula", "FORMULA"};
	public static List<String> FORMULA_NAME_CHOICES_ARRAY = Arrays.asList(FORMULA_NAME_CHOICES);
	
	public static List<String> INTENSITY_CHOICES_ARRAY = Arrays.asList(new String [] {"intensity", "intensities", "medintensity",  "medianintensity", "medintensities", "medianintensities"});
	
	public static String [] ANNOTATION_CHOICES = {"annotation", "annotations"};
	public static List<String> ANNOTATION_CHOICES_ARRAY = Arrays.asList(ANNOTATION_CHOICES);
	
	public static List<String> GROUP_ANNOTATION_CHOICES_ARRAY = Arrays.asList(new String [] {"otherannotationsingroup",  "groupannotations", "groupannotation", "grpannotation", "grpannotations"});
	
	public static String [] MODE_CHOICES = {"mode", "modes"};
	public static List<String> MODE_CHOICES_ARRAY = Arrays.asList(MODE_CHOICES);
	
	public static String [] CHARGE_CHOICES = {"charge", "charges", "z"};
	public static List<String> CHARGE_CHOICES_ARRAY = Arrays.asList(CHARGE_CHOICES);
	
	public static List<String> DERIVATION_CHOICES_ARRAY = Arrays.asList(new String [] {"derivation", "derivations"});
	public static List<String> BIN_CHOICES_ARRAY = Arrays.asList(new String [] {"bin", "binindex","binidx"});
	
	public static List<String> RTCLUSTER_CHOICES_ARRAY = Arrays.asList(new String [] {"newnewcluster", "rtcluster", "rtsubcluster", "retentiontimecluster"});
	public static List<String> REBINCLUSTER_CHOICES_ARRAY = Arrays.asList(new String [] {"newcluster", "rebincluster", "rebinsubcluster"});
	public static List<String> CLUSTER_CHOICES_ARRAY = Arrays.asList(new String [] {"corrcluster", "oldcluster", "cluster", "correlationcluster"});
	public static List<String> PUTATIVEMASS_CHOICES_ARRAY = Arrays.asList(new String [] {"derivedmolecularmass", 
			"derivedmolecularmasses", "putativemolecularmass", "derivedmass", "neutralmass", "putativemass"});
	public static List<String> ISOTOPE_CHOICES_ARRAY = Arrays.asList(new String [] {"isotope", "isotopes"});
	public static List<String> GROUP_ISOTOPE_CHOICES_ARRAY = Arrays.asList(new String [] {"otherisotopesingroup", "otherisotopesforgroup", "otherisotope", "otherisotopes", "othergroupisotopes", "othergrpisotopes"});
	
	public static List<String> KMD_CHOICES_ARRAY = Arrays.asList(new String [] {"kmd", "rmd"});
	public static List<String> CHARGE_CARRIER_CHOICES_ARRAY = Arrays.asList(new String [] {"chargecarrier", "carrier", "cc", "chargecarriers"});
	public static List<String> ADDUCT_CHOICES_ARRAY = Arrays.asList(new String [] {"adducts/nls", "adduct/nl", "adduct/nls", "adduct", "adducts", "adductnl", "adductornl"});
	
	public static List<String> MASS_ERROR_CHOICES_ARRAY = Arrays.asList(new String [] {"masserror", "error"});
	public static List<String> MOLECULAR_ION_CHOICES_ARRAY = Arrays.asList(new String [] {"molecularion", "molecularionnumber", "featuregroupnumber", "featuregroup", "featuregroupno"});
	public static List<String> FURTHER_ANNOTATION_CHOICES_ARRAY = Arrays.asList(new String [] {"furtherannotation", "otherannotation", "furtherannotations"});

	public static List<String> POSTPROCESS_UNANNOTATED_TAB_TAGS = Arrays.asList(new String [] {"non-annotated", "unannotated", "nonannotated", "noannotation", "unannotatedfeatures", "non-annotatedfeatures", "nonannotatedfeatures"});
	public static List<String> POSTPROCESS_REFMASS_TAB_TAGS = Arrays.asList(new String [] {"molecularions(putative)",
			"molecularions(possible)", "referencemasses", "putativemolecularions", "possiblemolecularions", "principalions", "principalion"});
	
	public static Integer N_OUTPUT_FIXED_COLUMN_LABELS = OUTPUT_FIXED_COLUMN_LABELS.length;
	public static final Integer OUTPUT_FIRST_ADDED_COLUMN_INDEX = N_OUTPUT_FIXED_COLUMN_LABELS + 1;
	
	public static String OUTPUT_CORRELATION_LABEL = "Correlations";
	public static String OUTPUT_MASS_DIFFS_LABEL = "Mass Diffs";

	public static final String TEMP_DIRECTORY = System.getProperty("java.io.tmpdir");
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public static final String HOME_DIRECTORY = System.getProperty("user.home", "~" + FILE_SEPARATOR);
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static String REPORT_FILE_EXTENSION = "_Report.xlsx";
	public static String POSTPROCESS_FILE_EXTENSION = "PostProcess";
	public static String BATCHPROCESS_FILE_EXTENSION = "BatchProcess";
	public static String POSTPROCESS_MERGE_FILE_EXTENSION = "PostProcessMerge";
	public static String CLIENT_RESULTS_DIRECTORY = HOME_DIRECTORY;
	public static String CLIENT_RESULTS_DIRECTORY_KEY = "results.directory";

	//public static String CONFIGURATION_DIRECTORY = "BatchMatchConfiguration";
	public static String INTERNAL_USE_ONLY_PROPS_FILE = "internal.use.only.props" + "." + POSTPROCESS_VERSION;
	
	//public static String PROPS_FILE = "batchmatch.props" + "." + BATCHMATCH_VERSION;
	//public static String BATCHPROCESS_PROPS_FILE = 
	

	public static final Color  TITLE_COLOR = new Color(0, 0, 205);
	public static final Double PROGRESS_BAR_WIDTH = 500.0;
	public static final Double DEISOTOPE_PROGRESS_WEIGHT = 15.0;
	public static final Double CLUSTERING_PROGRESS_WEIGHT = 45.0;
	public static final Double BINWISE_OUTPUT_WEIGHT = 20.0;
	public static final Double CLUSTERWISE_OUTPUT_WEIGHT = 2.0;
	
	public static final Double HYDROGEN_MASS = 1.00782;
	public static final Double CARBON_MASS = 12.0;
	public static final Double HC_BASE = 2 * HYDROGEN_MASS;
	public static final Double HC_STEP = 2 * HYDROGEN_MASS + CARBON_MASS;
	public static final Double KENDRICK_FACTOR = 14.0 / 14.01565;
	public static final Integer [] ISOTOPE_CHARGES = {2, 3, 1};
	public static final Integer [] ANNOTATION_CHARGES = {-3, -2, -1, 0, 1, 2, 3};
	public static final Double  [] ISOTOPE_MASS_DIFFS = {0.5017, 0.3345, 1.0034};
	public static final Integer MAX_ISOTOPE_CHARGE = ISOTOPE_CHARGES.length;
	public static final Integer DEFAULT_BATCH_LEVEL_REPORTED = 14;
	
	
	public static final Integer FIRST = 0;
	public static final Integer LAST = 1;
	
	public static final Integer EXPERIMENT = 0;
	public static final Integer LOOKUP = 1;
	public static final Integer ANNOTATION = 2;
	
	public static final Integer SORT_TYPE_RT = 0;
	public static final Integer SORT_TYPE_MASS = 1;
	public static final Integer SORT_TYPE_INTENSITY = 2;
	
	public static final Integer STYLE_BORING = 0;
	public static final Integer STYLE_INTEGER = 1;
	public static final Integer STYLE_NUMERIC = 2;
	public static final Integer STYLE_YELLOW = 3;
	public static final Integer STYLE_LIGHT_BLUE = 4;
	public static final Integer STYLE_LIGHT_GREEN = 5;
	public static final Integer STYLE_LAVENDER = 6;
	public static final Integer STYLE_INTEGER_GREY = 7;
	public static final Integer STYLE_NUMERIC_GREY = 8;
	public static final Integer STYLE_INTEGER_GREY_CENTERED = 9;
	public static final Integer STYLE_NUMERIC_GREY_CENTERED = 10;
	public static final Integer STYLE_BORING_LEFT = 11;
	public static final Integer STYLE_HEADER_WRAPPED = 12;
	public static final Integer STYLE_NUMERIC_SHORTER = 13;
	public static final Integer STYLE_BORING_RIGHT = 14;
	public static final Integer STYLE_NUMERIC_LAVENDER = 15;
	public static final Integer STYLE_BORING_GREY = 16;
	public static final Integer STYLE_YELLOW_LEFT = 17;
	public static final Integer STYLE_GREY_LEFT = 18;
	public static final Integer STYLE_NUMERIC_SHORTER_GREY = 19;
	public static final Integer STYLE_NUMERIC_SHORTEST_GREY = 20;
	public static final Integer STYLE_NUMERIC_SHORTEST = 21;
	public static final Integer STYLE_ORANGE = 22;

}
