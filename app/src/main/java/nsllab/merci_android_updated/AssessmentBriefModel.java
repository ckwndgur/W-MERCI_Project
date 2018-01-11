package nsllab.merci_android_updated;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Moallim on 12/13/2017.
 */

public class AssessmentBriefModel implements Serializable {
    public static final String IndividualAssessmentTypeIcon = "\uF015";
    public static final String BusinessAssessmentTypeIcon = "\uF0F2";
    public static final String InfrastructureAssessmentTypeIcon = "\uF072";
    protected String assessment_id;
    protected Double[] position;
    protected String name;
    protected String details;
    protected Boolean hasVideo;
    protected Boolean hasText;
    protected Boolean hasPhoto;
    protected String timestamp;
    protected String type;
    protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    protected static final String Individual = "Individual";
    protected static final String Business = "Business";
    protected static final String Infrastructure = "Infrastructure";

    public AssessmentBriefModel(String assessment_id, Double[] position, String name, String details, boolean hasText, boolean hasPhoto, boolean hasVideo, String type, String timestamp){
        this.assessment_id = assessment_id;
        this.position = position;
        this.name = name;
        this.details = details;
        this.hasText = hasText;
        this.hasPhoto = hasPhoto;
        this.hasVideo = hasVideo;
        this.timestamp = timestamp;
        this.type = type;
    }
}
