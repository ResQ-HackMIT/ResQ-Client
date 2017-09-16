package com.avontell.resq_client;

import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avontell.resq_client.domain.RescueID;

/**
 * An adapter for the rescue id recycler view
 * @author Aaron Vontell
 */
public class RescueIdAdapter extends RecyclerView.Adapter<RescueIdAdapter.ViewHolder> {

    private RescueID info;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleView;
        public ImageView iconView;
        public TextView valueView;

        public ViewHolder(LinearLayout cont) {
            super(cont);
            titleView = cont.findViewById(R.id.prop_title);
            valueView = cont.findViewById(R.id.prop_value);
            iconView = cont.findViewById(R.id.prop_icon);
        }

        public void setTitle(String title) {
            titleView.setText(title);
        }

        public void setValue(Object value, String title) {
            if (value == null) {
                valueView.setText("unknown");
            } else {

                switch (title) {
                    case "Age":
                        valueView.setText("" + value + " years old");
                        break;
                    case "Weight":
                        valueView.setText("" + value + " lbs.");
                        break;
                    case "Height":
                        int val = (int) value;
                        String res = (val / 12) + " ft. " + (val % 12) + " in.";
                        valueView.setText("" + res);
                        break;
                    default:
                        valueView.setText(value.toString());
                        break;
                }

            }
        }

        public void setIcon(String title) {
            switch (title) {
                case "Full Name":
                    iconView.setImageDrawable(iconView.getContext().getDrawable(R.drawable.person_icon));
                    break;
                case "Age":
                    iconView.setImageDrawable(iconView.getContext().getDrawable(R.drawable.age_icon));
                    break;
                case "Conditions":
                    iconView.setImageDrawable(iconView.getContext().getDrawable(R.drawable.condition_icon));
                    break;
                case "Medications":
                    iconView.setImageDrawable(iconView.getContext().getDrawable(R.drawable.med_icon));
                    break;
                case "Phone Number":
                    iconView.setImageDrawable(iconView.getContext().getDrawable(R.drawable.phone_icon));
                    break;
                case "Number of Children":
                    iconView.setImageDrawable(iconView.getContext().getDrawable(R.drawable.child_icon));
                    break;
                case "Number of Animals":
                    iconView.setImageDrawable(iconView.getContext().getDrawable(R.drawable.pets_icon));
                    break;
                case "Weight":
                    iconView.setImageDrawable(iconView.getContext().getDrawable(R.drawable.weight_icon));
                    break;
                case "Height":
                    iconView.setImageDrawable(iconView.getContext().getDrawable(R.drawable.height_icon));
                    break;
                case "Allergies":
                    iconView.setImageDrawable(iconView.getContext().getDrawable(R.drawable.vision_icon));
                    break;
                case "Note":
                    iconView.setImageDrawable(iconView.getContext().getDrawable(R.drawable.note_icon));
                    break;
                case "Spouse":
                    iconView.setImageDrawable(iconView.getContext().getDrawable(R.drawable.spouse_icon));
                    break;
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RescueIdAdapter(RescueID info) {
        this.info = info;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RescueIdAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.id_property, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.setTitle(info.getTitle(position));
        holder.setValue(info.getValue(position), info.getTitle(position));
        holder.setIcon(info.getTitle(position));

    }

    @Override
    public int getItemCount() {
        return info.getCount();
    }
}
