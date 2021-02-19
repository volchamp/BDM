package lsm.vpngate;

import android.app.Person;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

public class MainActivity extends AppCompatActivity {

    Handler handler=new Handler();
    List<VpnInfo> list=new ArrayList<>();
    ListView listView;

    IPAdapter ipAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) this.findViewById(R.id.listview);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String httpUrl="http://103.201.129.50:3260/cn/";
                String resultData ="";


                try {
                    URL url=new URL(httpUrl);
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
                    BufferedReader buffer = new BufferedReader(in);
                    String inputLine = null;
                    while (((inputLine = buffer.readLine()) != null)) {
                        resultData += inputLine + "\n";
                    }
                    in.close();
                    urlConn.disconnect();


                    Document doc = Jsoup.parse(resultData);

                    Elements tables = doc.select("table");
                    list.clear();
                    if(tables.size()==9)
                    {
                        Element table0=tables.get(7).child(0);
                        for (int i = 0; i < table0.children().size(); i++)
                        {
                            Element node= table0.child(i);
                            int size=node.childNodeSize();
                            if(node.tagName().equals("tr")&&size==11)
                            {
                                VpnInfo info = new VpnInfo();

                                String IP=node.childNodes().get(2).childNodes().get(2).outerHtml();
                                IP=IP.replace("<span style=\"font-size: 10pt;\">","");
                                IP=IP.replace("</span>","");

                                String sudu =  node.childNodes().get(4).childNodes().get(0).outerHtml();
                                sudu=sudu.replace("<b><span style=\"font-size: 10pt;\">","");
                                sudu=sudu.replace(" Mbps</span></b>","");


                                String nation = node.childNodes().get(1).childNodes().get(2).outerHtml();
                                nation=nation.replace("Korea Republic of","Korea");

                                if(node.childNodes().size()>6&&node.childNodes().get(7).childNodes().size()>0) {


                                    String href = node.childNodes().get(7).childNodes().get(0).attr("href");

                                    info.sudu=Double.parseDouble(sudu);
                                    info.IP=IP;
                                    info.href=href;

                                    int index=href.indexOf("&udp=");
                                    int index2=href.indexOf("&sid=");
                                    if(index>0&&index2>0)
                                    {
                                       if(node.childNode(5).childNodeSize()==5)
                                       {
                                           TextNode textNode=(TextNode)node.childNode(5).childNode(4);
                                           String text=textNode.text();
                                           if(text.contains("UDP: 支持"))
                                           {
                                               String UDP=href.substring(index+5,index2);
                                               info.udp=UDP;
                                               if(Integer.parseInt(UDP)>0) {
                                                   info.nation = nation;
                                                   list.add(info);
                                               }
                                           }


                                       }


                                    }




                                }
                            }



                        }
                        Log.i(this.getClass().getName(),list.size()+"");
                        handler.post(runnable);

                    }


                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }).start();



        try {
            //ping1("222.172.200.68");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }





    }

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            setTitle("获取有效IP:"+list.size());

            ipAdapter=  new IPAdapter(MainActivity.this,list);
            listView.setAdapter(ipAdapter);
            new Thread(new Runnable() {
                @Override
                public void run() {

                    for (int i=0;i<list.size();i++)
                    {
                        VpnInfo info= list.get(i);
                        String IP=info.IP;
                        int ss=ping(IP);
                        info.weichi=ss+"";
                        list.set(i,info);
                        final  int ii=i;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setTitle("获取有效IP:"+list.size()+",正在检查第:"+(ii+1)+"个");
                                ipAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Collections.sort(list, new Comparator<VpnInfo>() {

                                @Override
                                public int compare(VpnInfo info1, VpnInfo info2) {
                                    int weichi1=Integer.parseInt(info1.weichi);
                                    int weichi2=Integer.parseInt(info2.weichi);

                                    if (weichi1==-1)
                                    {
                                        weichi1=10000;
                                    }
                                    if (weichi2==-1)
                                    {
                                        weichi2=10000;
                                    }

                                    int diff =weichi1-weichi2;
                                    if (diff > 0) {
                                        return  1;
                                    }
                                    else if (diff <0)
                                    {
                                        return -1;
                                    }


                                    return 0;
                                }
                            });
                            listView.setAdapter(new IPAdapter(MainActivity.this,list));


                        }
                    });

                }
            }).start();

        }
    };

    public int ping(String ip)
    {
        try {
            String lost = new String();
            String delay = new String();
            Process p = Runtime.getRuntime().exec("ping -c 1 -w 1 " +ip);
            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = new String();
            while ((str = buf.readLine()) != null) {
                if (str.contains("packet loss")) {
                    int i = str.indexOf("received");
                    int j = str.indexOf("%");
                    System.out.println("丢包率:" + str.substring(i + 10, j + 1));
                    lost = str.substring(i + 10, j + 1);
                }
                if (str.contains("avg")) {
                    int i = str.indexOf("/", 20);
                    int j = str.indexOf(".", i);
                    System.out.println("延迟:" + str.substring(i + 1, j));
                    delay = str.substring(i + 1, j);
                    //delay = delay + "ms";
                    return Integer.parseInt(delay);
                }

            }
        }
        catch (Exception ex)
        {

        }
        return -1;
    }

    class IPAdapter extends BaseAdapter
    {
        List<VpnInfo> list;
        LayoutInflater  inflater;


        IPAdapter(Context context, List<VpnInfo> infos){
            this.list=infos;
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView textViewNation=null;
            TextView textViewIp=null;
            TextView textViewSudu=null;
            TextView textViewYanchi=null;

            TextView textViewUdp=null;

            if(convertView==null) {
                convertView = inflater.inflate(R.layout.item, null);

            }

            textViewNation=(TextView)convertView.findViewById(R.id.nation);
            textViewIp=(TextView)convertView.findViewById(R.id.ip);
            textViewSudu=(TextView)convertView.findViewById(R.id.sudu);
            textViewYanchi=(TextView)convertView.findViewById(R.id.yanchi);
            textViewUdp=(TextView)convertView.findViewById(R.id.udp);
            textViewNation.setText(list.get(position).nation);
            textViewIp.setText(list.get(position).IP);
            textViewSudu.setText(list.get(position).sudu+"");
            textViewYanchi.setText(list.get(position).weichi);

            textViewUdp.setText(list.get(position).udp);

            convertView.setOnClickListener(listener);



            return convertView;
        }
    }

    View.OnClickListener listener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            String content="###############################################################################\n" +
                    "# OpenVPN 2.0 Sample Configuration File\n" +
                    "# for PacketiX VPN / SoftEther VPN Server\n" +
                    "# \n" +
                    "# !!! AUTO-GENERATED BY SOFTETHER VPN SERVER MANAGEMENT TOOL !!!\n" +
                    "# \n" +
                    "# !!! YOU HAVE TO REVIEW IT BEFORE USE AND MODIFY IT AS NECESSARY !!!\n" +
                    "# \n" +
                    "# This configuration file is auto-generated. You might use this config file\n" +
                    "# in order to connect to the PacketiX VPN / SoftEther VPN Server.\n" +
                    "# However, before you try it, you should review the descriptions of the file\n" +
                    "# to determine the necessity to modify to suitable for your real environment.\n" +
                    "# If necessary, you have to modify a little adequately on the file.\n" +
                    "# For example, the IP address or the hostname as a destination VPN Server\n" +
                    "# should be confirmed.\n" +
                    "# \n" +
                    "# Note that to use OpenVPN 2.0, you have to put the certification file of\n" +
                    "# the destination VPN Server on the OpenVPN Client computer when you use this\n" +
                    "# config file. Please refer the below descriptions carefully.\n" +
                    "\n" +
                    "\n" +
                    "###############################################################################\n" +
                    "# Specify the type of the layer of the VPN connection.\n" +
                    "# \n" +
                    "# To connect to the VPN Server as a \"Remote-Access VPN Client PC\",\n" +
                    "#  specify 'dev tun'. (Layer-3 IP Routing Mode)\n" +
                    "#\n" +
                    "# To connect to the VPN Server as a bridging equipment of \"Site-to-Site VPN\",\n" +
                    "#  specify 'dev tap'. (Layer-2 Ethernet Bridgine Mode)\n" +
                    "\n" +
                    "dev tun\n" +
                    "\n" +
                    "\n" +
                    "###############################################################################\n" +
                    "# Specify the underlying protocol beyond the Internet.\n" +
                    "# Note that this setting must be correspond with the listening setting on\n" +
                    "# the VPN Server.\n" +
                    "# \n" +
                    "# Specify either 'proto tcp' or 'proto udp'.\n" +
                    "\n" +
                    "proto udp\n" +
                    "\n" +
                    "\n" +
                    "###############################################################################\n" +
                    "# The destination hostname / IP address, and port number of\n" +
                    "# the target VPN Server.\n" +
                    "# \n" +
                    "# You have to specify as 'remote <HOSTNAME> <PORT>'. You can also\n" +
                    "# specify the IP address instead of the hostname.\n" +
                    "# \n" +
                    "# Note that the auto-generated below hostname are a \"auto-detected\n" +
                    "# IP address\" of the VPN Server. You have to confirm the correctness\n" +
                    "# beforehand.\n" +
                    "# \n" +
                    "# When you want to connect to the VPN Server by using TCP protocol,\n" +
                    "# the port number of the destination TCP port should be same as one of\n" +
                    "# the available TCP listeners on the VPN Server.\n" +
                    "# \n" +
                    "# When you use UDP protocol, the port number must same as the configuration\n" +
                    "# setting of \"OpenVPN Server Compatible Function\" on the VPN Server.\n" +
                    "\n" +
                    "remote @IP @PORT\n" +
                    "\n" +
                    "\n" +
                    "###############################################################################\n" +
                    "# The HTTP/HTTPS proxy setting.\n" +
                    "# \n" +
                    "# Only if you have to use the Internet via a proxy, uncomment the below\n" +
                    "# two lines and specify the proxy address and the port number.\n" +
                    "# In the case of using proxy-authentication, refer the OpenVPN manual.\n" +
                    "\n" +
                    ";http-proxy-retry\n" +
                    ";http-proxy [proxy server] [proxy port]\n" +
                    "\n" +
                    "\n" +
                    "###############################################################################\n" +
                    "# The encryption and authentication algorithm.\n" +
                    "# \n" +
                    "# Default setting is good. Modify it as you prefer.\n" +
                    "# When you specify an unsupported algorithm, the error will occur.\n" +
                    "# \n" +
                    "# The supported algorithms are as follows:\n" +
                    "#  cipher: [NULL-CIPHER] NULL AES-128-CBC AES-192-CBC AES-256-CBC BF-CBC\n" +
                    "#          CAST-CBC CAST5-CBC DES-CBC DES-EDE-CBC DES-EDE3-CBC DESX-CBC\n" +
                    "#          RC2-40-CBC RC2-64-CBC RC2-CBC\n" +
                    "#  auth:   SHA SHA1 MD5 MD4 RMD160\n" +
                    "\n" +
                    "cipher AES-128-CBC\n" +
                    "auth SHA1\n" +
                    "\n" +
                    "\n" +
                    "###############################################################################\n" +
                    "# Other parameters necessary to connect to the VPN Server.\n" +
                    "# \n" +
                    "# It is not recommended to modify it unless you have a particular need.\n" +
                    "\n" +
                    "resolv-retry infinite\n" +
                    "nobind\n" +
                    "persist-key\n" +
                    "persist-tun\n" +
                    "client\n" +
                    "verb 3\n" +
                    "#auth-user-pass\n" +
                    "\n" +
                    "\n" +
                    "###############################################################################\n" +
                    "# The certificate file of the destination VPN Server.\n" +
                    "# \n" +
                    "# The CA certificate file is embedded in the inline format.\n" +
                    "# You can replace this CA contents if necessary.\n" +
                    "# Please note that if the server certificate is not a self-signed, you have to\n" +
                    "# specify the signer's root certificate (CA) here.\n" +
                    "\n" +
                    "<ca>\n" +
                    "-----BEGIN CERTIFICATE-----\n" +
                    "MIIF3jCCA8agAwIBAgIQAf1tMPyjylGoG7xkDjUDLTANBgkqhkiG9w0BAQwFADCB\n" +
                    "iDELMAkGA1UEBhMCVVMxEzARBgNVBAgTCk5ldyBKZXJzZXkxFDASBgNVBAcTC0pl\n" +
                    "cnNleSBDaXR5MR4wHAYDVQQKExVUaGUgVVNFUlRSVVNUIE5ldHdvcmsxLjAsBgNV\n" +
                    "BAMTJVVTRVJUcnVzdCBSU0EgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkwHhcNMTAw\n" +
                    "MjAxMDAwMDAwWhcNMzgwMTE4MjM1OTU5WjCBiDELMAkGA1UEBhMCVVMxEzARBgNV\n" +
                    "BAgTCk5ldyBKZXJzZXkxFDASBgNVBAcTC0plcnNleSBDaXR5MR4wHAYDVQQKExVU\n" +
                    "aGUgVVNFUlRSVVNUIE5ldHdvcmsxLjAsBgNVBAMTJVVTRVJUcnVzdCBSU0EgQ2Vy\n" +
                    "dGlmaWNhdGlvbiBBdXRob3JpdHkwggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIK\n" +
                    "AoICAQCAEmUXNg7D2wiz0KxXDXbtzSfTTK1Qg2HiqiBNCS1kCdzOiZ/MPans9s/B\n" +
                    "3PHTsdZ7NygRK0faOca8Ohm0X6a9fZ2jY0K2dvKpOyuR+OJv0OwWIJAJPuLodMkY\n" +
                    "tJHUYmTbf6MG8YgYapAiPLz+E/CHFHv25B+O1ORRxhFnRghRy4YUVD+8M/5+bJz/\n" +
                    "Fp0YvVGONaanZshyZ9shZrHUm3gDwFA66Mzw3LyeTP6vBZY1H1dat//O+T23LLb2\n" +
                    "VN3I5xI6Ta5MirdcmrS3ID3KfyI0rn47aGYBROcBTkZTmzNg95S+UzeQc0PzMsNT\n" +
                    "79uq/nROacdrjGCT3sTHDN/hMq7MkztReJVni+49Vv4M0GkPGw/zJSZrM233bkf6\n" +
                    "c0Plfg6lZrEpfDKEY1WJxA3Bk1QwGROs0303p+tdOmw1XNtB1xLaqUkL39iAigmT\n" +
                    "Yo61Zs8liM2EuLE/pDkP2QKe6xJMlXzzawWpXhaDzLhn4ugTncxbgtNMs+1b/97l\n" +
                    "c6wjOy0AvzVVdAlJ2ElYGn+SNuZRkg7zJn0cTRe8yexDJtC/QV9AqURE9JnnV4ee\n" +
                    "UB9XVKg+/XRjL7FQZQnmWEIuQxpMtPAlR1n6BB6T1CZGSlCBst6+eLf8ZxXhyVeE\n" +
                    "Hg9j1uliutZfVS7qXMYoCAQlObgOK6nyTJccBz8NUvXt7y+CDwIDAQABo0IwQDAd\n" +
                    "BgNVHQ4EFgQUU3m/WqorSs9UgOHYm8Cd8rIDZsswDgYDVR0PAQH/BAQDAgEGMA8G\n" +
                    "A1UdEwEB/wQFMAMBAf8wDQYJKoZIhvcNAQEMBQADggIBAFzUfA3P9wF9QZllDHPF\n" +
                    "Up/L+M+ZBn8b2kMVn54CVVeWFPFSPCeHlCjtHzoBN6J2/FNQwISbxmtOuowhT6KO\n" +
                    "VWKR82kV2LyI48SqC/3vqOlLVSoGIG1VeCkZ7l8wXEskEVX/JJpuXior7gtNn3/3\n" +
                    "ATiUFJVDBwn7YKnuHKsSjKCaXqeYalltiz8I+8jRRa8YFWSQEg9zKC7F4iRO/Fjs\n" +
                    "8PRF/iKz6y+O0tlFYQXBl2+odnKPi4w2r78NBc5xjeambx9spnFixdjQg3IM8WcR\n" +
                    "iQycE0xyNN+81XHfqnHd4blsjDwSXWXavVcStkNr/+XeTWYRUc+ZruwXtuhxkYze\n" +
                    "Sf7dNXGiFSeUHM9h4ya7b6NnJSFd5t0dCy5oGzuCr+yDZ4XUmFF0sbmZgIn/f3gZ\n" +
                    "XHlKYC6SQK5MNyosycdiyA5d9zZbyuAlJQG03RoHnHcAP9Dc1ew91Pq7P8yF1m9/\n" +
                    "qS3fuQL39ZeatTXaw2ewh0qpKJ4jjv9cJ2vhsE/zB+4ALtRZh8tSQZXq9EfX7mRB\n" +
                    "VXyNWQKV3WKdwrnuWih0hKWbt5DHDAff9Yk2dDLWKMGwsAvgnEzDHNb842m1R0aB\n" +
                    "L6KCq9NjRHDEjf8tM7qtj3u1cIiuPhnPQCjY/MiQu12ZIvVS5ljFH4gxQ+6IHdfG\n" +
                    "jjxDah2nGN59PRbxYvnKkKj9\n" +
                    "-----END CERTIFICATE-----\n" +
                    "\n" +
                    "</ca>\n" +
                    "\n" +
                    "\n" +
                    "###############################################################################\n" +
                    "# The client certificate file (dummy).\n" +
                    "# \n" +
                    "# In some implementations of OpenVPN Client software\n" +
                    "# (for example: OpenVPN Client for iOS),\n" +
                    "# a pair of client certificate and private key must be included on the\n" +
                    "# configuration file due to the limitation of the client.\n" +
                    "# So this sample configuration file has a dummy pair of client certificate\n" +
                    "# and private key as follows.\n" +
                    "\n" +
                    "<cert>\n" +
                    "-----BEGIN CERTIFICATE-----\n" +
                    "MIICxjCCAa4CAQAwDQYJKoZIhvcNAQEFBQAwKTEaMBgGA1UEAxMRVlBOR2F0ZUNs\n" +
                    "aWVudENlcnQxCzAJBgNVBAYTAkpQMB4XDTEzMDIxMTAzNDk0OVoXDTM3MDExOTAz\n" +
                    "MTQwN1owKTEaMBgGA1UEAxMRVlBOR2F0ZUNsaWVudENlcnQxCzAJBgNVBAYTAkpQ\n" +
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5h2lgQQYUjwoKYJbzVZA\n" +
                    "5VcIGd5otPc/qZRMt0KItCFA0s9RwReNVa9fDRFLRBhcITOlv3FBcW3E8h1Us7RD\n" +
                    "4W8GmJe8zapJnLsD39OSMRCzZJnczW4OCH1PZRZWKqDtjlNca9AF8a65jTmlDxCQ\n" +
                    "CjntLIWk5OLLVkFt9/tScc1GDtci55ofhaNAYMPiH7V8+1g66pGHXAoWK6AQVH67\n" +
                    "XCKJnGB5nlQ+HsMYPV/O49Ld91ZN/2tHkcaLLyNtywxVPRSsRh480jju0fcCsv6h\n" +
                    "p/0yXnTB//mWutBGpdUlIbwiITbAmrsbYnjigRvnPqX1RNJUbi9Fp6C2c/HIFJGD\n" +
                    "ywIDAQABMA0GCSqGSIb3DQEBBQUAA4IBAQChO5hgcw/4oWfoEFLu9kBa1B//kxH8\n" +
                    "hQkChVNn8BRC7Y0URQitPl3DKEed9URBDdg2KOAz77bb6ENPiliD+a38UJHIRMqe\n" +
                    "UBHhllOHIzvDhHFbaovALBQceeBzdkQxsKQESKmQmR832950UCovoyRB61UyAV7h\n" +
                    "+mZhYPGRKXKSJI6s0Egg/Cri+Cwk4bjJfrb5hVse11yh4D9MHhwSfCOH+0z4hPUT\n" +
                    "Fku7dGavURO5SVxMn/sL6En5D+oSeXkadHpDs+Airym2YHh15h0+jPSOoR6yiVp/\n" +
                    "6zZeZkrN43kuS73KpKDFjfFPh8t4r1gOIjttkNcQqBccusnplQ7HJpsk\n" +
                    "-----END CERTIFICATE-----\n" +
                    "\n" +
                    "</cert>\n" +
                    "\n" +
                    "<key>\n" +
                    "-----BEGIN RSA PRIVATE KEY-----\n" +
                    "MIIEpAIBAAKCAQEA5h2lgQQYUjwoKYJbzVZA5VcIGd5otPc/qZRMt0KItCFA0s9R\n" +
                    "wReNVa9fDRFLRBhcITOlv3FBcW3E8h1Us7RD4W8GmJe8zapJnLsD39OSMRCzZJnc\n" +
                    "zW4OCH1PZRZWKqDtjlNca9AF8a65jTmlDxCQCjntLIWk5OLLVkFt9/tScc1GDtci\n" +
                    "55ofhaNAYMPiH7V8+1g66pGHXAoWK6AQVH67XCKJnGB5nlQ+HsMYPV/O49Ld91ZN\n" +
                    "/2tHkcaLLyNtywxVPRSsRh480jju0fcCsv6hp/0yXnTB//mWutBGpdUlIbwiITbA\n" +
                    "mrsbYnjigRvnPqX1RNJUbi9Fp6C2c/HIFJGDywIDAQABAoIBAERV7X5AvxA8uRiK\n" +
                    "k8SIpsD0dX1pJOMIwakUVyvc4EfN0DhKRNb4rYoSiEGTLyzLpyBc/A28Dlkm5eOY\n" +
                    "fjzXfYkGtYi/Ftxkg3O9vcrMQ4+6i+uGHaIL2rL+s4MrfO8v1xv6+Wky33EEGCou\n" +
                    "QiwVGRFQXnRoQ62NBCFbUNLhmXwdj1akZzLU4p5R4zA3QhdxwEIatVLt0+7owLQ3\n" +
                    "lP8sfXhppPOXjTqMD4QkYwzPAa8/zF7acn4kryrUP7Q6PAfd0zEVqNy9ZCZ9ffho\n" +
                    "zXedFj486IFoc5gnTp2N6jsnVj4LCGIhlVHlYGozKKFqJcQVGsHCqq1oz2zjW6LS\n" +
                    "oRYIHgECgYEA8zZrkCwNYSXJuODJ3m/hOLVxcxgJuwXoiErWd0E42vPanjjVMhnt\n" +
                    "KY5l8qGMJ6FhK9LYx2qCrf/E0XtUAZ2wVq3ORTyGnsMWre9tLYs55X+ZN10Tc75z\n" +
                    "4hacbU0hqKN1HiDmsMRY3/2NaZHoy7MKnwJJBaG48l9CCTlVwMHocIECgYEA8jby\n" +
                    "dGjxTH+6XHWNizb5SRbZxAnyEeJeRwTMh0gGzwGPpH/sZYGzyu0SySXWCnZh3Rgq\n" +
                    "5uLlNxtrXrljZlyi2nQdQgsq2YrWUs0+zgU+22uQsZpSAftmhVrtvet6MjVjbByY\n" +
                    "DADciEVUdJYIXk+qnFUJyeroLIkTj7WYKZ6RjksCgYBoCFIwRDeg42oK89RFmnOr\n" +
                    "LymNAq4+2oMhsWlVb4ejWIWeAk9nc+GXUfrXszRhS01mUnU5r5ygUvRcarV/T3U7\n" +
                    "TnMZ+I7Y4DgWRIDd51znhxIBtYV5j/C/t85HjqOkH+8b6RTkbchaX3mau7fpUfds\n" +
                    "Fq0nhIq42fhEO8srfYYwgQKBgQCyhi1N/8taRwpk+3/IDEzQwjbfdzUkWWSDk9Xs\n" +
                    "H/pkuRHWfTMP3flWqEYgW/LW40peW2HDq5imdV8+AgZxe/XMbaji9Lgwf1RY005n\n" +
                    "KxaZQz7yqHupWlLGF68DPHxkZVVSagDnV/sztWX6SFsCqFVnxIXifXGC4cW5Nm9g\n" +
                    "va8q4QKBgQCEhLVeUfdwKvkZ94g/GFz731Z2hrdVhgMZaU/u6t0V95+YezPNCQZB\n" +
                    "wmE9Mmlbq1emDeROivjCfoGhR3kZXW1pTKlLh6ZMUQUOpptdXva8XxfoqQwa3enA\n" +
                    "M7muBbF0XN7VO80iJPv+PmIZdEIAkpwKfi201YB+BafCIuGxIF50Vg==\n" +
                    "-----END RSA PRIVATE KEY-----\n" +
                    "\n" +
                    "</key>";


            TextView textViewNation= v.findViewById(R.id.nation);
            TextView textViewIp= v.findViewById(R.id.ip);
            TextView textViewUdp= v.findViewById(R.id.udp);
            String IP=textViewIp.getText().toString();
            String UDP=textViewUdp.getText().toString();
            String nation=textViewNation.getText().toString();

            Date date=new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String day=sdf.format(date);

            String filename=day+"_"+IP+"_"+nation+".ovpn";
            String string=content.replace("@IP",IP);
            string=string.replace("@PORT",UDP);
            FileOutputStream outputStream;

            File file =new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), filename);


            try {

                if(!file.exists())
                {
                    file.createNewFile();
                }

                outputStream = new FileOutputStream(file);
                outputStream.write(string.getBytes());
                outputStream.close();
                Toast.makeText(MainActivity.this,"文件保存成功",Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }

        }
    };


}
