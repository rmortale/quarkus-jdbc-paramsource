#!/bin/bash

DEPLOY_NAMESPACE=${1:-devt}
DEPLOY_TAG=${2:-main}
DEPLOY_ACTION=${3:-deploy}

git checkout $DEPLOY_TAG
git pull
kubens $DEPLOY_NAMESPACE

if [[ "$DEPLOY_ACTION" == "undeploy" ]]; then
  printf '\nUNDEPLOY environment %s...\n\n' "$DEPLOY_NAMESPACE"
  kubectl delete -k k8s/overlays/$DEPLOY_NAMESPACE
else
  printf '\nDeploying tag %s to environment %s...\n\n' "$DEPLOY_TAG" "$DEPLOY_NAMESPACE"
  kubectl apply -k k8s/overlays/$DEPLOY_NAMESPACE
fi

git checkout main





