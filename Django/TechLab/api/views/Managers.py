from django.conf import settings
from django.http.response import JsonResponse
from django.views.generic import View
from django.shortcuts import get_object_or_404
import datetime

from api.models import (User)
import json


class Managers(View):
    def get(self, request, *args, **kwargs):

        managers = User.objects.filter(is_manager=True)

        return JsonResponse(json.loads(json.dumps([manager.to_json('_state') for manager in managers])), safe=False)

class Manager(View):
    def get(self, request, pk, *args, **kwargs):
        manager = get_object_or_404(User, id=pk, is_manager=True)

        return JsonResponse(json.loads(json.dumps(manager.to_json('_state'))), safe=False)

    def put(self, request, pk, *args, **kwargs):
        manager = get_object_or_404(User, id=pk)

        manager.is_manager = True
        manager.save()

        return JsonResponse(json.loads('{"success": "true", "message": "The user has been added as Manager. "}'), safe=False)

    def delete(self, request, pk, *args, **kwargs):
        manager = get_object_or_404(User, id=pk)

        manager.is_manager = False
        manager.save()

        return JsonResponse(json.loads('{"success": "true", "message": "The user has been removed as Manager. "}'), safe=False)
