package necer.expandtextdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import necer.expandtextview.ExpandTextView;

public class MainActivity extends AppCompatActivity {

    private ExpandTextView expand_text;
    private ExpandTextView expand_text2;

    private String text = "以上各项标准，除另有指明外，均指诉讼案件一审或一审或仲裁案件的收费标准。理未办理一审而办理审的，按一审标准收取;曾办理一审又办理二审的，按一审标准酌减收取;曾经代理仲裁的，诉讼一、二审阶段按仲裁阶段标准酌减收取;执行案件，按照一审或二审的收费标准收取;发回重审的案件，按一审或二审标准酌减收取。";
    private String text2 = "曾阶段标准酌减收取;执行案件，按照按照按照按照按照按照按照按照按照按照按照按照按照按照按照按照按照一审或二审的或二审的或二审的或二审的或二审的或二审的或二审的或二审的或二审的或二审的或二审的或二审的或二审的或二审的或二审的二审的或二审的或二审的或二审的";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expand_text = (ExpandTextView) findViewById(R.id.expand_text);
        expand_text.setText(text);

        expand_text2 = (ExpandTextView) findViewById(R.id.expand_text2);
        expand_text2.setText(text2);
    }


}
