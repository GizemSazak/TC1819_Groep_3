from django.conf import settings
from django.http.response import JsonResponse
from django.views.generic import View
from django.db.models import Prefetch
from django.contrib.auth.models import User as AuthUser

from api.models import (Book, Electronic, Writer, Category, Publisher)

import json


# Create your views here.
class GetAllItems(View):
    def get(self, request, *args, **kwargs):
        allBooks = Book.objects.all()
        allElectronics = Electronic.objects.all()

        return JsonResponse(json.loads('[{ "books": %s}, {"electronics" : %s}]' % (
            json.dumps([book.to_json('_state', 'item_ptr_id', 'writers') for book in allBooks]),
            json.dumps([electronic.to_json('_state', 'item_ptr_id') for electronic in allElectronics]))), safe=False)

class GetAllBooks(View):
    def get(self, request, *args, **kwargs):
        allBooks = Book.objects.all()
        return JsonResponse(json.loads(json.dumps([book.to_json('_state', 'item_ptr_id') for book in allBooks])), safe=False)

    def post(self, request, *args, **kwargs):
        if 'username' in request.POST:
            admin = AuthUser.objects.filter(username=request.POST['username']).first() if \
                AuthUser.objects.filter(username=request.POST['username']).count() > 0 else None

            if admin is not None:
                book = Book.objects.create(borrow_days=request.POST['borrow_days'],
                                           description=request.POST['description'],
                                           title=request.POST['title'],
                                           isbn=request.POST['isbn'],
                                           publisher=Publisher.objects.get(id=request.POST['publisher']),
                                           stock=request.POST['stock'],)
                book.save()

                return JsonResponse('{"success": "true", "message": "The item has been created."}',
                                    safe=False, status=200)

        return JsonResponse('', safe=False, status=401)

class GetAllElectronics(View):
    def get(self, request, *args, **kwargs):
        allElectronics = Electronic.objects.all()
        return JsonResponse(json.loads(json.dumps([electronic.to_json('_state', 'item_ptr_id') for electronic in allElectronics])), safe=False)

    def post(self, request, *args, **kwargs):
        if 'username' in request.POST:
            admin = AuthUser.objects.filter(username=request.POST['username']).first() if \
                AuthUser.objects.filter(username=request.POST['username']).count() > 0 else None

            if admin is not None:
                electronic = Electronic.objects.create(borrow_days=request.POST['borrow_days'],
                                                       description=request.POST['description'],
                                                       product_id=request.POST['product_id'],
                                                       name=request.POST['name'],
                                                       category=Category.objects.get(id=request.POST['category']),
                                                       stock=request.POST['stock'],
                                                       broken=request.POST['broken'])
                electronic.save()

                return JsonResponse('{"success": "true", "message": "The item has been created."}',
                                    safe=False, status=200)

        return JsonResponse('', safe=False, status=401)
