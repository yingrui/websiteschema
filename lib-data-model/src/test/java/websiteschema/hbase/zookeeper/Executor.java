/**
 * A simple example program to use DataMonitor to start and stop executables based on a znode. The program watches the
 * specified znode and saves the data that corresponds to the znode in the filesystem. It also starts the specified
 * program with the specified arguments when the znode exists and kills the program if the znode goes away.
 */
package websiteschema.hbase.zookeeper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Executor
        implements Watcher, Runnable, DataMonitor.DataMonitorListener {

    String znode;
    DataMonitor dm;
    ZooKeeper zk;
    String filename;
    String exec[];
    Process child;

    public Executor(String hostPort, String znode, String filename) throws KeeperException, IOException, InterruptedException {
        this.filename = filename;
        zk = new ZooKeeper(hostPort, 3000, this);
        System.out.println("-----------------------"+zk.getChildren("/", true));
        dm = new DataMonitor(zk, znode, null, this);
    }

    /**
     * *************************************************************************
     * We do process any events ourselves, we just need to forward them on.
     *
     * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.proto.WatcherEvent)
     */
    @Override
    public void process(WatchedEvent event) {
        dm.process(event);
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (!dm.dead) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }

    static class StreamWriter extends Thread {

        OutputStream os;
        InputStream is;

        StreamWriter(InputStream is, OutputStream os) {
            this.is = is;
            this.os = os;
            start();
        }

        @Override
        public void run() {
            byte b[] = new byte[80];
            int rc;
            try {
                while ((rc = is.read(b)) > 0) {
                    os.write(b, 0, rc);
                }
            } catch (IOException e) {
            }

        }
    }

    @Override
    public void exists(byte[] data) {
        if (data != null) {
            try {
                FileOutputStream fos = new FileOutputStream(filename);
                fos.write(data);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

     /**
     * @param args
     */
    public static void main(String args[]) throws Exception {
//        if (args.length < 3) {
//            System.err.println("USAGE: Executor hostPort znode filename program [args ...]");
//            System.exit(2);
//        }
//        String hostPort = args[0];
//        String znode = args[1];
//        String filename = args[2];

        String hostPort = "hadoop-hbase1:2181";
        String znode = "/master";
        String filename = "zookeeper_out.log";
        try {
            new Executor(hostPort, znode, filename).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}