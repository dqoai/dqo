#!/bin/sh
#
# Copyright © 2021 DQO.ai (support@dqo.ai)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

export DQO_HOME=$(dirname $0)/home

if [ -f .DQO_USER_HOME ]
then
    export DQO_USER_HOME=.
    return
fi

export DQO_USER_HOME="$(dirname $0)/userhome"

if [ ! -d $DQO_USER_HOME ]
then
    mkdir $DQO_USER_HOME
fi
