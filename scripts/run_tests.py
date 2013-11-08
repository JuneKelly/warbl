#!/usr/bin/env python
import subprocess
import pexpect
import time
from xvfbwrapper import Xvfb


print '<>> Starting test server and running specs...'

with Xvfb() as display:
    srv = pexpect.spawn('lein with-profile test trampoline ring server-headless')

    time.sleep(5)

    subprocess.call(' '.join(['lein', 'with-profile', 'test', 'spec']),
                    shell=True)

    print '<<>>'

    srv.terminate()
    print '<>> Test server stopped...'
