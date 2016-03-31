package com.praveenbhati.agrovision.adapter;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.praveenbhati.agrovision.model.Lead;
import com.praveenbhati.agrovision.R;

import java.util.ArrayList;

/**
 *@author  Bhati on 11/15/2015.
 */
public class LeadAdapter extends BaseAdapter {

    Context context;
    ArrayList<Lead> leadArrayList;

    public LeadAdapter(Context context, ArrayList<Lead> leadArrayList) {
        this.context = context;
        this.leadArrayList = leadArrayList;
    }

    @Override
    public int getCount() {
        return leadArrayList.size();
    }


    @Override
    public Object getItem(int position) {
        return leadArrayList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context, R.layout.row_lead,null);
            new ViewHolder(convertView);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        final Lead lead = (Lead) getItem(position);

        viewHolder.tvName.setText(lead.getFirstName()+" "+lead.getMiddleName()+" "+lead.getLastName());
        viewHolder.tvMobile.setText(lead.getMobileNumber());
        viewHolder.tvProduct.setText(lead.getProductName());
        viewHolder.tvSubProduct.setText(lead.getSubProductName());
        viewHolder.tvAmount.setText(String.valueOf(lead.getTotalAmount()));
        viewHolder.tvStatus.setText(lead.getStatus());
        viewHolder.tvDate.setText(lead.getContactDate());
        viewHolder.ibStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Under Construction", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Under Construction", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.tvMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:"+lead.getMobileNumber()));
                dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(dialIntent);
            }
        });

        viewHolder.ibaddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ContentProviderOperation> ops =
                        new ArrayList<ContentProviderOperation>();

                int rawContactID = ops.size();
                // Adding insert operation to operations list
                // to insert a new raw contact in the table ContactsContract.RawContacts
                ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build());

                // Adding insert operation to operations list
                // to insert display name in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, lead.getFirstName() + " " + lead.getMiddleName()+" "+lead.getLastName() )
                        .build());

                // Adding insert operation to operations list
                // to insert Mobile Number in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,lead.getMobileNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());

                try{
                    // Executing all the insert operations as a single database transaction
                    context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                    Toast.makeText(context, "Contact is successfully added", Toast.LENGTH_SHORT).show();
                }catch (RemoteException e) {
                    e.printStackTrace();
                }catch (OperationApplicationException e) {
                    e.printStackTrace();
                }

            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView tvName,tvMobile,tvProduct,tvSubProduct,tvAmount,tvStatus,tvDate;
        ImageButton ibEdit,ibStatus,ibaddContact;

        public ViewHolder(View convertView) {
            tvName = (TextView) convertView.findViewById(R.id.row_lead_tv_name);
            tvMobile = (TextView) convertView.findViewById(R.id.row_lead_tv_mobile);
            tvProduct = (TextView) convertView.findViewById(R.id.row_lead_tv_product);
            tvSubProduct = (TextView) convertView.findViewById(R.id.row_lead_tv_subproduct);
            tvAmount = (TextView) convertView.findViewById(R.id.row_lead_tv_amount);
            tvStatus = (TextView) convertView.findViewById(R.id.row_lead_tv_status);
            tvDate = (TextView) convertView.findViewById(R.id.row_lead_tv_date);

            ibEdit = (ImageButton) convertView.findViewById(R.id.row_lead_ib_edit);
            ibStatus = (ImageButton) convertView.findViewById(R.id.row_lead_ib_status);
            ibaddContact = (ImageButton) convertView.findViewById(R.id.row_lead_ib_addcontact);
            convertView.setTag(this);
        }
    }
}
