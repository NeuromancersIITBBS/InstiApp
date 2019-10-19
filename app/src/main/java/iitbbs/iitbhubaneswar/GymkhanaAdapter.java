package iitbbs.iitbhubaneswar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GymkhanaAdapter extends RecyclerView.Adapter<GymkhanaAdapter.OfficeBearer> {

    private List<String> officeBearerLinks;
    private Context context;

    public class OfficeBearer extends RecyclerView.ViewHolder {
        public TextView post = null;
        public TextView name = null;
        public ImageView contact = null;
        public ImageView email = null;
        public ImageView pic = null;

        public OfficeBearer(View view) {
            super(view);
            post = (TextView) view.findViewById(iitbbs.iitbhubaneswar.R.id.post);
            name = (TextView) view.findViewById(iitbbs.iitbhubaneswar.R.id.name);
            contact = (ImageView) view.findViewById(iitbbs.iitbhubaneswar.R.id.contact);
            email = (ImageView) view.findViewById(iitbbs.iitbhubaneswar.R.id.email);
            pic = (ImageView) view.findViewById(iitbbs.iitbhubaneswar.R.id.pic);
        }
    }

    public GymkhanaAdapter(List<String> officeBearerLinks, Context context) {
        this.officeBearerLinks = officeBearerLinks;
        this.context = context;
    }

    @Override
    public OfficeBearer onCreateViewHolder(ViewGroup parent, int viewType) {
        View officeBearer = LayoutInflater.from(parent.getContext())
                .inflate(iitbbs.iitbhubaneswar.R.layout.office_bearer, parent, false);

        return new OfficeBearer(officeBearer);
    }

    @Override
    public void onBindViewHolder(final OfficeBearer holder, int position) {
        String link = officeBearerLinks.get(position);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReferencePost = firebaseDatabase.getReference("gymkhana_office_bearers/" + link + "/post");
        databaseReferencePost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.post.setText(String.valueOf(dataSnapshot.getValue()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        DatabaseReference databaseReferenceName = firebaseDatabase.getReference("gymkhana_office_bearers/" + link + "/name");
        databaseReferenceName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.name.setText(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReferenceContact = firebaseDatabase.getReference("gymkhana_office_bearers/" + link + "/contact");
        databaseReferenceContact.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.contact.setContentDescription(String.valueOf(dataSnapshot.getValue()));
                holder.contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + holder.contact.getContentDescription()));
                        context.startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        DatabaseReference databaseReferenceEmail = firebaseDatabase.getReference("gymkhana_office_bearers/" + link + "/email");
        databaseReferenceEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.email.setContentDescription(String.valueOf(dataSnapshot.getValue()));
                holder.email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + holder.email.getContentDescription()));
                        context.startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        DatabaseReference databaseReferencePic = firebaseDatabase.getReference("gymkhana_office_bearers/" + link + "/pic");
        databaseReferencePic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Picasso.get().load(String.valueOf(dataSnapshot.getValue())).into(holder.pic);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public int getItemCount() {
        return officeBearerLinks.size();
    }
}
