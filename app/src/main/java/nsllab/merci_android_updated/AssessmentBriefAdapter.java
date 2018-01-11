package nsllab.merci_android_updated;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Moallim on 12/13/2017.
 */

public class AssessmentBriefAdapter extends RecyclerView.Adapter<AssessmentBriefAdapter.MyViewHolder> {
    protected List<AssessmentBriefModel> assessmentBriefModelList;
    protected ItemClickRecyclerViewInterface listener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView timestamp;
        public TextView details;
        public TextView typeIcon;
        public IconView hasText;
        public IconView hasPhoto;
        public IconView hasVideo;

        public MyViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.assessment_brief_name);
            timestamp = (TextView) view.findViewById(R.id.assessment_brief_timestamp);
            details = (TextView) view.findViewById(R.id.assessment_brief_details);
            typeIcon = (TextView) view.findViewById(R.id.assessment_brief_type_icon);
            hasText = (IconView) view.findViewById(R.id.assessment_brief_has_text);
            hasPhoto = (IconView) view.findViewById(R.id.assessment_brief_has_photo);
            hasVideo = (IconView) view.findViewById(R.id.assessment_brief_has_video);
        }
    }

    public AssessmentBriefAdapter(List<AssessmentBriefModel> assessmentBriefModelList, ItemClickRecyclerViewInterface listener){
        this.assessmentBriefModelList = assessmentBriefModelList;
        this.listener = listener;
    }

    @Override
    public AssessmentBriefAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessment_brief_layout, parent, false);
        return new AssessmentBriefAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AssessmentBriefAdapter.MyViewHolder holder, int position) {
        final AssessmentBriefModel assessmentBriefModel = assessmentBriefModelList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClickRecyclerView(assessmentBriefModel);
            }
        });
        holder.name.setText(assessmentBriefModel.name);
        holder.timestamp.setText(assessmentBriefModel.timestamp);
        holder.details.setText(assessmentBriefModel.details);
        switch (assessmentBriefModel.type){
            case AssessmentBriefModel.Individual:
                holder.typeIcon.setText(assessmentBriefModel.IndividualAssessmentTypeIcon);
                break;
            case AssessmentBriefModel.Business:
                holder.typeIcon.setText(assessmentBriefModel.BusinessAssessmentTypeIcon);
                break;
            case AssessmentBriefModel.Infrastructure:
                holder.typeIcon.setText(assessmentBriefModel.InfrastructureAssessmentTypeIcon);
                break;
        }
        if(assessmentBriefModel.hasText)
            holder.hasText.setVisibility(View.VISIBLE);
        else
            holder.hasText.setVisibility(View.GONE);
        if(assessmentBriefModel.hasPhoto)
            holder.hasPhoto.setVisibility(View.VISIBLE);
        else
            holder.hasPhoto.setVisibility(View.GONE);
        if(assessmentBriefModel.hasVideo)
            holder.hasVideo.setVisibility(View.VISIBLE);
        else
            holder.hasVideo.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount(){
        return assessmentBriefModelList.size();
    }
}
