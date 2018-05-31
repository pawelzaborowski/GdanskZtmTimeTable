package si.uni_lj.student.pz8285.ztmtimetable.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import si.uni_lj.student.pz8285.ztmtimetable.R;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Popup");
        final CharSequence[] items = {"item A", "Item b"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item){
                    /////Do stuff;
                    default:
                        break;
                }
            }
        }).show();
        setContentView(R.layout.activity_share);
    }
}
