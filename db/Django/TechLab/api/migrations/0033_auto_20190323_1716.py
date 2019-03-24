# Generated by Django 2.1.7 on 2019-03-23 16:16

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0032_auto_20190323_1654'),
    ]

    operations = [
        migrations.AddField(
            model_name='item',
            name='image',
            field=models.FileField(blank=True, upload_to='', verbose_name='post_image'),
        ),
        migrations.AlterField(
            model_name='borrowitem',
            name='borrow_date',
            field=models.DateTimeField(default=datetime.datetime(2019, 3, 23, 17, 16, 16, 87469)),
        ),
        migrations.AlterField(
            model_name='borrowitem',
            name='return_date',
            field=models.DateTimeField(default=datetime.datetime(2019, 3, 23, 17, 16, 16, 87469)),
        ),
    ]
