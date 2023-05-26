#! /bin/bash -e


trap "set +x; echo ===== EXIT User Data script =====" EXIT


# Captured to /var/log/cloud-init-output.log
exec | logger -t user-data -s > /dev/console 2>&1


echo "===== Display template parameters ====="

echo "domain=${domain}"
echo "email=${email}"
echo "target_ip=${target_ip}"

set -x
PROXY_IP=$(curl http://169.254.169.254/latest/meta-data/public-ipv4)
set +x
echo "PROXY_IP=$PROXY_IP (this host)"


echo "===== Install software ====="
set -x
amazon-linux-extras install -y epel
yum install -y nginx certbot
set +x


echo "===== Generate certificate ====="
set -x
while ! certbot certonly --standalone --non-interactive --agree-tos --email "${email}" --domains "${domain}"
do
    set +x
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    echo "!! Domain auth verification failed."
    echo "!! Please update your DNS now, to make sure ${domain} points to $PROXY_IP".
    echo "!! Script will now sleep for 5 minutes, then will try to run verification again."
    echo "!! There are 5 failing attempts allowed before locking for 1 hour."
    echo "!! For details see: https://letsencrypt.org/docs/failed-validation-limit/"
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    set -x
    sleep $((5 * 60))
done
set +x


echo "===== Update config ====="
set -x
mv /etc/nginx/nginx.conf /etc/nginx/nginx.conf.orig
cat << EOF > nginx.conf
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log;
pid /run/nginx.pid;

include /usr/share/nginx/modules/*.conf;

events {
    worker_connections 1024;
}

http {
    log_format  main  '\$remote_addr - \$remote_user [\$time_local] "\$request" '
                      '\$status \$body_bytes_sent "\$http_referer" '
                      '"\$http_user_agent" "\$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;

    sendfile            on;
    tcp_nopush          on;
    tcp_nodelay         on;
    keepalive_timeout   65;
    types_hash_max_size 2048;

    include             /etc/nginx/mime.types;
    default_type        application/octet-stream;

    include /etc/nginx/conf.d/*.conf;

    server {
        listen       80 default_server;
        listen       [::]:80 default_server;
        server_name  _;
        return       301 https://\$host\$request_uri;
    }

    server {
        listen       443 ssl http2 default_server;
        listen       [::]:443 ssl http2 default_server;
        server_name  _;

        ssl_certificate "/etc/letsencrypt/live/${domain}/cert.pem";
        ssl_certificate_key "/etc/letsencrypt/live/${domain}/privkey.pem";
        ssl_session_cache shared:SSL:1m;
        ssl_session_timeout  10m;
        ssl_ciphers HIGH:!aNULL:!MD5;
        ssl_prefer_server_ciphers on;

        include /etc/nginx/default.d/*.conf;

        location / {
            proxy_pass       http://${target_ip};
            proxy_set_header Host \$host;
            proxy_set_header X-Real-IP \$remote_addr;
            proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto \$scheme;
            proxy_buffering  off;
        }

        error_page 404 /404.html;
            location = /40x.html {
        }

        error_page 500 502 503 504 /50x.html;
            location = /50x.html {
        }
    }
}
EOF
mv nginx.conf /etc/nginx/nginx.conf
set +x

echo "===== Start server ====="
set -x
service nginx start
set +x


echo "===== Add renewal to cron ====="
set -x
echo "1 1 1,8,16,20,25 * * (echo ========== && date && nginx -s stop && certbot renew && nginx) >> /var/log/cron-cert-renewal.log 2>&1" >> cron-cert-renewal
crontab cron-cert-renewal
rm cron-cert-renewal
set +x


echo "===== Command to revoke certificate ====="
echo "certbot revoke --cert-path \"/etc/letsencrypt/live/${domain}/fullchain.pem\""
