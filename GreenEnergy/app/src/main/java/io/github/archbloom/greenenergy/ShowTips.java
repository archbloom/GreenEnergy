package io.github.archbloom.greenenergy;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by archbloom on 5/3/16.
 */
public class ShowTips extends Fragment {
    View view;
    TextView textView;
    Button next,prev;
    String tip;
    String[] strs;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tips, container, false);

        textView = (TextView) view.findViewById(R.id.showTips);
        next = (Button)view.findViewById(R.id.next);
        prev = (Button)view.findViewById(R.id.prev);

        strs = getResources().getStringArray(R.array.tips);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int maxIndex = strs.length;
                int generatedIndex = random.nextInt(maxIndex);
                textView.setText(strs[generatedIndex]);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int maxIndex = strs.length;
                int generatedIndex = random.nextInt(maxIndex);
                textView.setText(strs[generatedIndex]);
            }
        });

        return view;
    }
}
